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
import android.widget.Toast;

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

    public static final String SCANDIT_LICENSE_KEY = "AfwuMFaLSPCHEAeHuEDFEhYllu3KGapcFiIrikUhqmyXatqWhnoPT15ydc5LaKfgOxahxBh1po93dS/VYDLlE+EsU+Z/fugqFBEd+8R+VZV+eIVuGiu9IE1nr3HWFLYyLh8OvO0MTfPcdGViTpCcUpD9HyyxoxAJS4h1XF5vivNJ4TrwzK2MLnm7y1mbUqQjnEBbYDVVjsO/QbvkNOJLOO0NJ05kzF6/W0lQpAHvLnP33uogptL/yOoY5qjzjpeB0tH/bIhnZOcMsAM6igo9dHuxP7lMYGi3hQyn/xCj8t2RzlIWnxkfIlsREVzGFJpVUDCDMclprOJ0ZWzz+/1mxreb60UReQSG6VyUMrBzdEwB0Gss/1KkujafBz+8QHk76YY8GZBz+EhYCBpjnAZoJh5bedHKhLbao8t6ICk7gLY2VHL0FweagR5q0OuOxYCsi3SkGhUcNk4u/GGR/1Ub+CXeW/bnjwK808VPqfxzYKRMPqBaFZhBO/COr1liyvU689+8EGXwBwtbIKADhNY9BlOBcLfD4trWr4IrQupPKb//eUXYeHD5RJDSsBSu5d2Ti/PXjKysEQKWuR63fuVvChzHZAjzGQB88f27/3WnwB540W3QcQlMu243SgORYjDucCce+HosIc0Kk7YF7EAvzxOj/rg+467mRjj7EinMHPQ/I5r2+JjRb02iP2lsF49rXGPrv0bw/BQJCZ0+lliIYVgyyKmhmODxSNmJFtBJgWrYTRvtUHX4R5cmUioHFytjSWGILyrlSQL4gy3KXBBGPrEWMLGgdlgT4h5rpj+Y6k3+1ljPJuXW";

    private DataCaptureContext dataCaptureContext;
    private BarcodeCapture barcodeCapture;
    private Camera camera;
    private DataCaptureView dataCaptureView;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize and start the barcode recognition.
        initializeAndStartBarcodeScanning();
    }

    private void initializeAndStartBarcodeScanning() {
        // Create data capture context using your license key.
        dataCaptureContext = DataCaptureContext.forLicenseKey(SCANDIT_LICENSE_KEY);

        // Use the default camera and set it as the frame source of the context.
        // The camera is off by default and must be turned on to start streaming frames to the data
        // capture context for recognition.
        // See resumeFrameSource and pauseFrameSource below.
        camera = Camera.getDefaultCamera();
        if (camera != null) {
            // Use the recommended camera settings for the BarcodeCapture mode.
            camera.applySettings(BarcodeCapture.createRecommendedCameraSettings());
            dataCaptureContext.setFrameSource(camera);
        } else {
            throw new IllegalStateException("Sample depends on a camera, which failed to initialize.");
        }

        // The barcode capturing process is configured through barcode capture settings
        // which are then applied to the barcode capture instance that manages barcode recognition.
        BarcodeCaptureSettings barcodeCaptureSettings = new BarcodeCaptureSettings();

        // The settings instance initially has all types of barcodes (symbologies) disabled.
        // For the purpose of this sample we enable a very generous set of symbologies.
        // In your own app ensure that you only enable the symbologies that your app requires as
        // every additional enabled symbology has an impact on processing times.
        HashSet<Symbology> symbologies = new HashSet<>();
        symbologies.add(Symbology.EAN13_UPCA);
        symbologies.add(Symbology.EAN8);
        symbologies.add(Symbology.UPCE);
        symbologies.add(Symbology.QR);
        symbologies.add(Symbology.DATA_MATRIX);
        symbologies.add(Symbology.CODE39);
        symbologies.add(Symbology.CODE128);
        symbologies.add(Symbology.INTERLEAVED_TWO_OF_FIVE);

        barcodeCaptureSettings.enableSymbologies(symbologies);

        // Some linear/1d barcode symbologies allow you to encode variable-length data.
        // By default, the Scandit Data Capture SDK only scans barcodes in a certain length range.
        // If your application requires scanning of one of these symbologies, and the length is
        // falling outside the default range, you may need to adjust the "active symbol counts"
        // for this symbology. This is shown in the following few lines of code for one of the
        // variable-length symbologies.
        SymbologySettings symbologySettings =
                barcodeCaptureSettings.getSymbologySettings(Symbology.CODE39);

        HashSet<Short> activeSymbolCounts = new HashSet<>(
                Arrays.asList(new Short[]{7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20}));

        symbologySettings.setActiveSymbolCounts(activeSymbolCounts);

        // Create new barcode capture mode with the settings from above.
        barcodeCapture = BarcodeCapture.forDataCaptureContext(dataCaptureContext, barcodeCaptureSettings);

        // Register self as a listener to get informed whenever a new barcode got recognized.
        barcodeCapture.addListener(this);

        // To visualize the on-going barcode capturing process on screen, setup a data capture view
        // that renders the camera preview. The view must be connected to the data capture context.
        dataCaptureView = DataCaptureView.newInstance(this, dataCaptureContext);

        // Add a barcode capture overlay to the data capture view to render the location of captured
        // barcodes on top of the video preview.
        // This is optional, but recommended for better visual feedback.
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
        // Switch camera off to stop streaming frames.
        // The camera is stopped asynchronously and will take some time to completely turn off.
        // Until it is completely stopped, it is still possible to receive further results, hence
        // it's a good idea to first disable barcode capture as well.
        barcodeCapture.setEnabled(false);
        camera.switchToDesiredState(FrameSourceState.OFF, null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check for camera permission and request it, if it hasn't yet been granted.
        // Once we have the permission the onCameraPermissionGranted() method will be called.
        requestCameraPermission();
    }

    @Override
    public void onCameraPermissionGranted() {
        resumeFrameSource();
    }

    private void resumeFrameSource() {
        dismissScannedCodesDialog();

        // Switch camera on to start streaming frames.
        // The camera is started asynchronously and will take some time to completely turn on.
        barcodeCapture.setEnabled(true);
        camera.switchToDesiredState(FrameSourceState.ON, null);
    }

    private void dismissScannedCodesDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private void showResult(final String result) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query product = reference.child("Product").child(userId).child("product").orderByKey().equalTo(result);
        product.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                   /* Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                    intent.putExtra("productKey", result);
                    startActivity(intent);
                    finish();*/
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
        // Stop recognizing barcodes for as long as we are displaying the result. There won't be any
        // new results until the capture mode is enabled again. Note that disabling the capture mode
        // does not stop the camera, the camera continues to stream frames until it is turned off.
        barcodeCapture.setEnabled(false);

        // If you are not disabling barcode capture here and want to continue scanning, consider
        // setting the codeDuplicateFilter when creating the barcode capture settings to around 500
        // or even -1 if you do not want codes to be scanned more than once.

        // Get the human readable name of the symbology and assemble the result to be shown.
        String symbology = SymbologyDescription.create(barcode.getSymbology()).getReadableName();
        final String result = barcode.getData();

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
