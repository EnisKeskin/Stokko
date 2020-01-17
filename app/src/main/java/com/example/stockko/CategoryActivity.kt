package com.example.stockko

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_category.*

@SuppressLint("Registered")
class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

    }

    fun btnCategoryAdd(v: View) {
        var category = tvCategory1.text.toString()
        if (category.isNotEmpty()) {
            var reference = FirebaseDatabase.getInstance().reference
            var userId = FirebaseAuth.getInstance().currentUser?.uid
            var categoryDatabase =
                reference.child("Product").child(userId.toString()).child("category")
            categoryDatabase.push().child("name").setValue(category).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Başarılı")
                } else {
                    println("Başarısız")
                }
            }
        }
    }

}