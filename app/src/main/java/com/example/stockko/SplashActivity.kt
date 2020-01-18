package com.example.stockko

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stockko.dataClass.Category
import com.example.stockko.dataClass.Titles
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {

            val reference = FirebaseDatabase.getInstance().reference

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            println(userId)
            val query = reference.child("Product")
                .child(userId.toString())
                .child("category")

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                //iptal olma durumunda
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                //verinin değiştiği yani verinin alındığı yer
                override fun onDataChange(p0: DataSnapshot) {
                    Titles.clearTitle()
                    for (singleTitle in p0.children) {
                        var value = singleTitle.getValue(Category::class.java)
                        value?.key = singleTitle.key
                        value?.let { it1 -> Titles.setTitle(it1) }
                    }
                    val intent =
                        Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            })
        }

    }
}
