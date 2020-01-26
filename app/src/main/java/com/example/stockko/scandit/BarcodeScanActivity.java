/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.stockko.scandit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.stockko.DetailActivity;
import com.example.stockko.ProductActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.scandit.datacapture.barcode.capture.*;
import com.scandit.datacapture.barcode.data.Barcode;
import com.scandit.datacapture.barcode.data.Symbology;
import com.scandit.datacapture.barcode.data.SymbologyDescription;
import com.scandit.datacapture.barcode.ui.overlay.BarcodeCaptureOverlay;
import com.scandit.datacapture.core.capture.DataCaptureContext;
import com.scandit.datacapture.core.data.FrameData;
import com.scandit.datacapture.core.source.Camera;
import com.scandit.datacapture.core.source.FrameSourceState;
import com.scandit.datacapture.core.ui.DataCaptureView;
import com.scandit.datacapture.core.ui.viewfinder.RectangularViewfinder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import static android.content.DialogInterface.OnClickListener;

public class BarcodeScanActivity
        extends CameraPermissionActivity implements BarcodeCaptureListener {

    public static final String SCANDIT_LICENSE_KEY = "-- ENTER BARCODE KEY HERE --";

    private DataCaptureContext dataCaptureContext;
    private BarcodeCapture barcodeCapture;
    private Camera camera;
    private DataCaptureView dataCaptureView;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeAndStartBarcodeScanning();
    }

    private void initializeAndStartBarcodeScanning() {
        //lisans keyi giriliyor
        dataCaptureContext = DataCaptureContext.forLicenseKey(SCANDIT_LICENSE_KEY);

       //Kamerayı tüm çerçecevede göstermek için
        camera = Camera.getDefaultCamera();
        if (camera != null) {
           //barcode çercevesinin gösterildiği yer
            camera.applySettings(BarcodeCapture.createRecommendedCameraSettings());
            dataCaptureContext.setFrameSource(camera);
        } else {
            throw new IllegalStateException("Başlatılamayan bir kameraya bağlıdır.");
        }

       //barcode ayarlarının oluşturulduğu ve başlatıldığı yer
        BarcodeCaptureSettings barcodeCaptureSettings = new BarcodeCaptureSettings();

        //Hangi barcode tiplerinini tarayacığı yer
        HashSet<Symbology> symbologies = new HashSet<>();
        symbologies.add(Symbology.EAN13_UPCA);
        symbologies.add(Symbology.EAN8);
        symbologies.add(Symbology.UPCE);
        symbologies.add(Symbology.QR);
        symbologies.add(Symbology.DATA_MATRIX);
        symbologies.add(Symbology.CODE39);
        symbologies.add(Symbology.CODE128);
        symbologies.add(Symbology.INTERLEAVED_TWO_OF_FIVE);
        symbologies.add(Symbology.INTERLEAVED_TWO_OF_FIVE);

        barcodeCaptureSettings.enableSymbologies(symbologies);

        //Belirli uzunluklardaki barcodeları tanımalamak için alltaki list oluşturulmuştur
        SymbologySettings symbologySettings =
                barcodeCaptureSettings.getSymbologySettings(Symbology.CODE39);

        HashSet<Short> activeSymbolCounts = new HashSet<>(
                Arrays.asList(new Short[]{7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20}));

        symbologySettings.setActiveSymbolCounts(activeSymbolCounts);

        //ayarlar tamamlandıktan sonra yeni ayarlar barcode eklendi
        barcodeCapture = BarcodeCapture.forDataCaptureContext(dataCaptureContext, barcodeCaptureSettings);

        //barcode listeye eklendi
        barcodeCapture.addListener(this);

        //Yapılan ayarların görselleştirilmesi ile ilgili ayar
        dataCaptureView = DataCaptureView.newInstance(this, dataCaptureContext);

       //barcode okunduğu ve kaydediğildiği yer
        BarcodeCaptureOverlay overlay = BarcodeCaptureOverlay.newInstance(barcodeCapture, dataCaptureView);
        overlay.setViewfinder(new RectangularViewfinder());

        setContentView(dataCaptureView);
    }

    @Override
    protected void onPause() {
        pauseFrameSource();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        barcodeCapture.removeListener(this);
        dataCaptureContext.removeMode(barcodeCapture);
        super.onDestroy();
    }

    private void pauseFrameSource() {
        //kamera uyumsuzluğu veya zorla kapatma durumunda çalışır
        barcodeCapture.setEnabled(false);
        camera.switchToDesiredState(FrameSourceState.OFF, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //kamera iznini kontrol ediyor
        requestCameraPermission();
    }

    @Override
    public void onCameraPermissionGranted() {
        resumeFrameSource();
    }

    private void resumeFrameSource() {
        dismissScannedCodesDialog();

        //kameranın başlatıldığı yer
        barcodeCapture.setEnabled(true);
        camera.switchToDesiredState(FrameSourceState.ON, null);
    }

    private void dismissScannedCodesDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
    //verinin gösterildiğiy er
    private void showResult(final String result) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query product = reference.child("Product").child(userId).child("product").orderByKey().equalTo(result);
        product.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //verinin yönlendirildiği yer
                    dialog = builder.setCancelable(false)
                            .setTitle(result)
                            .setPositiveButton("Ürüne git",
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            barcodeCapture.setEnabled(true);
                                            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                                            intent.putExtra("productKey", result);
                                            finish();
                                            startActivity(intent);
                                        }
                                    })
                            .setNegativeButton("İptal et",
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            barcodeCapture.setEnabled(true);
                                        }
                                    })
                            .create();
                    dialog.show();
                } else {
                    dialog = builder.setCancelable(false)
                            .setTitle(result)
                            .setPositiveButton("Ürünü ekle",
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            barcodeCapture.setEnabled(true);
                                            Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                                            intent.putExtra("barcode", result);
                                            finish();
                                            startActivity(intent);
                                        }
                                    })
                            .setNegativeButton("İptal et",
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            barcodeCapture.setEnabled(true);
                                        }
                                    })
                            .create();
                    dialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBarcodeScanned(
            @NonNull BarcodeCapture barcodeCapture,
            @NonNull BarcodeCaptureSession session,
            @NonNull FrameData frameData
    ) {
        if (session.getNewlyRecognizedBarcodes().isEmpty()) return;

        Barcode barcode = session.getNewlyRecognizedBarcodes().get(0);
        //barkod numarasının geldiği yer
        barcodeCapture.setEnabled(false);


        SymbologyDescription.create(barcode.getSymbology()).getReadableName();
        final String result = barcode.getData();
        //thread işi bittiğinde görüntüye veriyi gönderiyor.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showResult(result);
            }
        });
    }

    @Override
    public void onSessionUpdated(@NonNull BarcodeCapture barcodeCapture,
                                 @NonNull BarcodeCaptureSession session, @NonNull FrameData data) {
    }

    @Override
    public void onObservationStarted(@NonNull BarcodeCapture barcodeCapture) {
    }

    @Override
    public void onObservationStopped(@NonNull BarcodeCapture barcodeCapture) {
    }
}
