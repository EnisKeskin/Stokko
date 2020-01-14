package com.example.stockko

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvKayitOl.setOnClickListener {

            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnGirisYap.setOnClickListener {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword("eniskeskin61@hotmail.com", "123456")
                .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                    override fun onComplete(p0: Task<AuthResult>) = if (p0.isSuccessful) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Başarılı Giriş." + FirebaseAuth.getInstance().currentUser?.email,
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent1 = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent1)
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Hatalı Giriş" + p0.exception?.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                })
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

    }


}
