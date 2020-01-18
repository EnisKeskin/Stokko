package com.example.stockko

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.stockko.dataClass.Category
import com.example.stockko.dataClass.Titles
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvKayitOl.setOnClickListener {

            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        if (FirebaseAuth.getInstance().currentUser == null) {
            btnGirisYap.setOnClickListener {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword("eniskeskin61@hotmail.com", "123456")
                    .addOnCompleteListener { p0 ->
                        if (p0.isSuccessful) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Başarılı Giriş." + FirebaseAuth.getInstance().currentUser?.email,
                                Toast.LENGTH_SHORT
                            ).show()
                            loggedIn()

                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Hatalı Giriş" + p0.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
//            if (etSifre.text.isNotEmpty() && etSifre.text.isNotEmpty()){
//
//                FirebaseAuth.getInstance().signInWithEmailAndPassword(etMail.text.toString(),etSifre.text.toString())
//                    .addOnCompleteListener(object:OnCompleteListener<AuthResult>{
//                        override fun onComplete(p0: Task<AuthResult>) {
//
//                            if(p0.isSuccessful){
//                                Toast.makeText(this@LoginActivity, "Başarılı Giriş."+FirebaseAuth.getInstance().currentUser?.email,Toast.LENGTH_SHORT).show()
//                                var intent1 = Intent(this@LoginActivity,MainActivity::class.java)
//                                startActivity(intent1)
//                            }
//
//                            else{
//                                Toast.makeText(this@LoginActivity, "Hatalı Giriş"+ p0.exception?.message,Toast.LENGTH_SHORT).show()
//
//                            }
//                        }
//
//                    })
//
//            }
//            else{
//                Toast.makeText(this@LoginActivity, "Boş alanları doldurunuz",Toast.LENGTH_SHORT).show()
//
//            }

            }
        } else {
            loggedIn()
        }
    }

    fun loggedIn() {
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
                    Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }

}
