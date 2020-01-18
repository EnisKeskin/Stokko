package com.example.stockko

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.stockko.dataClass.Products
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_detail.*
import java.time.LocalDateTime

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailActivity : AppCompatActivity() {
    //detay activity oluduğu yer

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent != null) {
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
                    //hata olduğundan main dönecek
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
                //image ve category eklenecek
                override fun onDataChange(p0: DataSnapshot) {

                    val value = p0.getValue(Products::class.java)!!
                    etId.setText(p0.key)
                    etName.setText(value.name)
                    etPiece.setText(value.piece)
                    etDetail.setText(value.detail)
                }
            })
        } else {
            //hata olduğundan main dönecek
        }

        btnProductSave.setOnClickListener {
            val key = intent.getStringExtra("productKey")
            val productValue = Products()
            productValue.key = key
            productValue.name = etName.text.toString()
            productValue.piece = etPiece.text.toString()
            productValue.detail = etDetail.text.toString()
            productValue.date = LocalDateTime.now().toString()
            val reference = FirebaseDatabase.getInstance().reference
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val product = reference
                .child("Product")
                .child(userId.toString())
                .child("product")
                .child(key)
            product.setValue(productValue)
        }

    }

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
}
