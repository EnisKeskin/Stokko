@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.stockko

import android.R.*
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.stockko.dataClass.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_page.*
import java.time.LocalDateTime

@SuppressLint("Registered")
class ProductActivity : AppCompatActivity(), ProductİmageFragment.onProductImageListener {

    var permission = false
    var galleryImageUri: Uri? = null
    var cameraImageBitmap: Bitmap? = null

    override fun getImagePath(ImagePath1: Uri?) {

        galleryImageUri = ImagePath1
        Picasso.with(this).load(galleryImageUri).resize(170, 150).into(ivProductİmage)

    }

    override fun getImageBitmap(bitmap: Bitmap) {

        cameraImageBitmap = bitmap
        ivProductİmage.setImageBitmap(bitmap)
    }
//sözde buradan yapacaktım ama sıkıştırma yapmadım o yüzden anlamadım

    /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         if (requestCode == 150 && resultCode == 100) {
             if(data == null || data.data == null){
                 return
             }


             try {
                 val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, galleryImageUri)
                 ivProductİmage.setImageBitmap(bitmap)
             } catch (e: IOException) {
                 e.printStackTrace()
             }
         }
     }

     private fun uploadImage(){
         if(galleryImageUri != null){
             var storeReference = FirebaseStorage.getInstance().getReference()
             var ref = storeReference?.child("uploads/" + UUID.randomUUID().toString())
             val uploadTask = ref?.putFile(galleryImageUri!!)

             val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                 if (!task.isSuccessful) {
                     task.exception?.let {
                         throw it
                     }
                 }
                 return@Continuation ref.downloadUrl
             })?.addOnCompleteListener { task ->
                 if (task.isSuccessful) {
                     val downloadUri = task.result
                     addUploadRecordToDb(downloadUri.toString())
                 } else {
                     // Handle failures
                 }
             }?.addOnFailureListener{

             }
         }else{
             Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
         }
     }


     private fun addUploadRecordToDb(uri: String){
         val db = FirebaseFirestore.getInstance()

         val data = HashMap<String, Any>()
         data["imageUrl"] = uri

         db.collection("posts")
             .add(data)
             .addOnSuccessListener { documentReference ->
                 Toast.makeText(this, "Saved to DB", Toast.LENGTH_LONG).show()
             }
             .addOnFailureListener { e ->
                 Toast.makeText(this, "Error saving to DB", Toast.LENGTH_LONG).show()
             }
     }
 */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_page)
        //sol üstte sola doğru oku aktif eder.
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        spinnerAddItem()

        if (intent != null) {
            etBarcodId.setText(intent.getStringExtra("barcode"))
        }

        ivProductİmage.setOnClickListener {

            if (permission) {
                var dialog = ProductİmageFragment()
                dialog.show(supportFragmentManager, "Fotoğraf seç")
            } else {
                permissionWant()
            }
        }
    }


    private fun permissionWant() {
        var permissions = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
        )

        if (ContextCompat.checkSelfPermission(
                this,
                permissions[0]
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                permissions[1]
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                permissions[2]
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            permission = true
        } else {
            ActivityCompat.requestPermissions(this, permissions, 150)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 150) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                var dialog = ProductİmageFragment()
                dialog.show(supportFragmentManager, "Fotoğraf seç")
            } else {
                Toast.makeText(this, "Tüm izinleri vermelisiniz.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun checkTextView(): Boolean {
        if (etName.text.toString().isNotEmpty() && etPrice.text.toString().isNotEmpty() && etBarcodId.text.isNotEmpty()) {
            return true
        }
        return false
    }

    private fun spinnerAddItem() {
        val category = Titles.getTitle()
        val arrayCategory = arrayOfNulls<String>(category.size + 1)
        arrayCategory[0] = ""
        for (i in 1 until category.size + 1) {
            arrayCategory[i] = category[i - 1].name
        }
        val adapter = ArrayAdapter(this, layout.simple_spinner_dropdown_item, arrayCategory)
        adapter.setDropDownViewResource(layout.simple_spinner_dropdown_item)
        spnCategory.adapter = adapter
    }

    fun selectCategory(selectedCategory: String): String {
        val category = Titles.getTitle()
        var databaseCategory: String = ""
        for (i in 0 until category.size) {
            if (category[i].name == selectedCategory) {
                databaseCategory = category[i].key.toString()
            }
        }
        return databaseCategory
    }

    //geri dönme okuna basınca otomatikman bir önceki yere atar.
    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addProduct(view: View) {
        if (checkTextView()) {
            val addedProductItem = Products()
            addedProductItem.categoryId = selectCategory(spnCategory.selectedItem.toString())
            addedProductItem.date = LocalDateTime.now().toString()
            addedProductItem.detail = etDetail.text.toString()
            addedProductItem.name = etName.text.toString()
            addedProductItem.piece = etPrice.text.toString()
            addedProductItem.image = "image"

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val reference = FirebaseDatabase.getInstance().reference
            val product = reference.child("Product").child(userId.toString()).child("product")
            product.child(etBarcodId.text.toString()).setValue(addedProductItem)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        println("Veri eklendi")
                        var intent = Intent(this, DetailActivity::class.java)
                        intent.putExtra("productKey", etBarcodId.text.toString())
                        startActivity(intent)
                    } else {
                        println("veri eklenemedi")
                    }
                }
        }
    }


}




