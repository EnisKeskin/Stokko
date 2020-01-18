@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.stockko

import android.R.*
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.stockko.dataClass.Category
import com.example.stockko.dataClass.Products
import com.example.stockko.dataClass.Titles
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.product_page.*
import java.time.LocalDateTime

@SuppressLint("Registered")
class ProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_page)
        //sol üstte sola doğru oku aktif eder.
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        spinnerAddItem()

        if (intent != null) {
            etBarcodId.setText(intent.getStringExtra("barcode"))
        }
    }

    private fun checkTextView(): Boolean {
        if (etName.text.toString().isNotEmpty() && etPiece.text.toString().isNotEmpty() && etBarcodId.text.isNotEmpty()) {
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
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addProduct(view: View) {
        if (checkTextView()) {
            val addedProductItem = Products()
            addedProductItem.categoryId = selectCategory(spnCategory.selectedItem.toString())
            addedProductItem.date = LocalDateTime.now().toString()
            addedProductItem.detail = etDetail.text.toString()
            addedProductItem.name = etName.text.toString()
            addedProductItem.piece = etPiece.text.toString()
            addedProductItem.image = "image"

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val reference = FirebaseDatabase.getInstance().reference
            val product = reference.child("Product").child(userId.toString()).child("product")
            product.child(etBarcodId.text.toString()).setValue(addedProductItem)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        println("Veri eklendi")
                    } else {
                        println("veri eklenemedi")
                    }
                }
        }
    }
}




