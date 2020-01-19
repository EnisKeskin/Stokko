package com.example.stockko

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment


class ProductİmageFragment : DialogFragment() {

    lateinit var tvGallerySelect : TextView
    lateinit var tvCameraSelect : TextView

    interface onProductImageListener{

        fun getImagePath(ImagePath1:Uri?)
        fun getImageBitmap(bitmap: Bitmap)
    }

    lateinit var mProductImageListener: onProductImageListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var v= inflater.inflate(R.layout.fragment_product_image, container, false)

        tvGallerySelect = v.findViewById(R.id.tvGalleryImageSelect)
        tvCameraSelect = v.findViewById(R.id.tvCameraİmage)


        tvGallerySelect.setOnClickListener {

            var intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type="image/*"
            startActivityForResult(intent,100)//Galeri açılacak
        }


        tvCameraSelect.setOnClickListener {

            var intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,200)

        }

        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Galeriden resim seçiliyor
        if(requestCode==100 && resultCode==Activity.RESULT_OK && data !=null){

            var gallerySelectImagePath = data.data
            Log.e("Resim Yolu","gallerySelectImagePath")
            mProductImageListener.getImagePath(gallerySelectImagePath)
            dismiss()


        }

        //kameradan
        else if(requestCode==200 && resultCode==Activity.RESULT_OK && data !=null){

            var cameraImageTake : Bitmap
            cameraImageTake = data.extras?.get("data") as Bitmap
            mProductImageListener.getImageBitmap(cameraImageTake)
            dialog?.dismiss()
        }


    }

    override fun onAttach(context: Context) {

        mProductImageListener=activity as onProductImageListener

        super.onAttach(context)
    }

}
