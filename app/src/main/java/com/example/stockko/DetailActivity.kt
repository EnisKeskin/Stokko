package com.example.stockko

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.stockko.dataClass.Products
import com.example.stockko.dataClass.Titles
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.etDetail
import kotlinx.android.synthetic.main.activity_detail.etName
import kotlinx.android.synthetic.main.activity_detail.spnCategory
import java.time.LocalDateTime
import java.util.*


@Suppress(
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
class DetailActivity : AppCompatActivity(), ProductİmageFragment.onProductImageListener {
    //detay activity oluduğu yer
    var permission = false
    var galleryImageUri: Uri? = null
    var cameraImageBitmap: Bitmap? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (intent != null) {
            val key = intent.getStringExtra("productKey")
            if (key.isNotEmpty()) {
                spinnerAddItem()
                val reference = FirebaseDatabase.getInstance().reference
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val product = reference
                    .child("Product")
                    .child(userId.toString())
                    .child("product")
                    .child(key)
                product.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        //hata olduğundan main dönecek
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    //image ve category eklenecek
                    override fun onDataChange(p0: DataSnapshot) {
                        val value = p0.getValue(Products::class.java)!!
                        etId.setText(p0.key)
                        val category = Titles.getTitle()
                        repeat(category.size) {
                            if (category[it].key == value.categoryId) {
                                spnCategory.setSelection(it + 1)
                            }
                        }
                        etName.setText(value.name)
                        nbrPiece.setText(value.piece)
                        etDetail.setText(value.detail)

                        val storageRef = FirebaseStorage.getInstance().reference
                        if (value.image.toString() != "") {
                            val pathReference =
                                storageRef.child("images").child(value.image.toString())
                            val oneMegabyte: Long = 1024 * 1024
                            pathReference.getBytes(oneMegabyte).addOnSuccessListener {
                                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                                ivProductImage.setImageBitmap(bitmap)
                            }.addOnFailureListener {
                                // Handle any errors
                            }
                        }
                    }
                })
            } else {
                goToMain()
            }

        } else {
            goToMain()
        }

        btnProductSave.setOnClickListener {
            val key = intent.getStringExtra("productKey")
            val productValue = Products()
            val category = Titles.getTitle()

            if (galleryImageUri != null) {
                val storeReference = FirebaseStorage.getInstance().reference
                val imageKey = UUID.randomUUID().toString()
                val ref = storeReference.child("images/$imageKey")
                val file = galleryImageUri
                val uploadTask = ref.putFile(file!!)

                uploadTask.addOnFailureListener {
                    println("Eklenemedi")
                }.addOnSuccessListener {
                    repeat(category.size) {
                        if (category[it].name == spnCategory.selectedItem.toString()) {
                            productValue.categoryId = category[it].key
                        }
                    }
                    productValue.key = key
                    productValue.name = etName.text.toString()
                    productValue.piece = nbrPiece.text.toString()
                    productValue.detail = etDetail.text.toString()
                    productValue.date = LocalDateTime.now().toString()
                    productValue.image = imageKey

                    val reference = FirebaseDatabase.getInstance().reference
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    val product = reference
                        .child("Product")
                        .child(userId.toString())
                        .child("product")
                        .child(key)
                    product.setValue(productValue)
                    Toast.makeText(this@DetailActivity, "Ürün Güncellendi", Toast.LENGTH_LONG)
                        .show()
                }

            } else {
                repeat(category.size) {
                    if (category[it].name == spnCategory.selectedItem.toString()) {
                        productValue.categoryId = category[it].key
                    }
                }
                productValue.key = key
                productValue.name = etName.text.toString()
                productValue.piece = nbrPiece.text.toString()
                productValue.detail = etDetail.text.toString()
                productValue.date = LocalDateTime.now().toString()
                productValue.image = ""

                val reference = FirebaseDatabase.getInstance().reference
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val product = reference
                    .child("Product")
                    .child(userId.toString())
                    .child("product")
                    .child(key)
                product.setValue(productValue)
                Toast.makeText(this@DetailActivity, "Ürün Güncellendi", Toast.LENGTH_LONG)
                    .show()
            }
        }

        ivProductImage.setOnClickListener {

            if (permission) {
                val dialog = ProductİmageFragment()
                dialog.show(supportFragmentManager, "Fotoğraf seç")
            } else {
                permissionWant()
            }
        }

        btnDelete.setOnClickListener {
            val key = intent.getStringExtra("productKey")
            val reference = FirebaseDatabase.getInstance().reference
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val product = reference
                .child("Product")
                .child(userId.toString())
                .child("product")
                .child(key)
            product.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val image = p0.getValue(Products::class.java)?.image
                    val storageRef = FirebaseStorage.getInstance().reference
                    storageRef.child("images").child(image.toString()).delete()
                        .addOnSuccessListener {
                            Toast.makeText(this@DetailActivity, "Ürün silindi", Toast.LENGTH_LONG)
                                .show()
                            product.removeValue()
                            goToMain()
                        }
                }

            })
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


    private fun spinnerAddItem() {
        val category = Titles.getTitle()
        val arrayCategory = arrayOfNulls<String>(category.size + 1)
        arrayCategory[0] = ""
        for (i in 1 until category.size + 1) {
            arrayCategory[i] = category[i - 1].name
        }
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayCategory)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnCategory.adapter = adapter
    }


    override fun onSupportNavigateUp(): Boolean {
        goToMain()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        goToMain()
        super.onBackPressed()
    }

    override fun getImagePath(ImagePath1: Uri?) {
        galleryImageUri = ImagePath1
        Picasso.with(this).load(galleryImageUri).resize(170, 150).into(ivProductImage)
    }

    override fun getImageBitmap(bitmap: Bitmap) {
        cameraImageBitmap = bitmap
        ivProductImage.setImageBitmap(bitmap)
    }

    fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
