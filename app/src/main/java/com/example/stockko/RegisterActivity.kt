package com.example.stockko

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.stockko.dataClass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        btnKaydol.setOnClickListener {
            if (etMail.text.isNotEmpty() && etSifre.text.isNotEmpty() && etSifreTekrar.text.isNotEmpty() && etAd.text.isNotEmpty() && etMagazaAdi.text.isNotEmpty() && etMagazaAdres.text.isNotEmpty()) {
                if (etSifre.text.toString() == etSifreTekrar.text.toString()) {
                    newUserRegistration(
                        etMail.text.toString(),
                        etSifre.text.toString(),
                        etAd.text.toString(),
                        etMagazaAdi.text.toString(),
                        etMagazaAdres.text.toString()
                    )
                } else {
                    Toast.makeText(this, "Şifreler aynı değil", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Boş alanları doldurunuz", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun newUserRegistration(
        mail: String,
        password: String,
        username: String,
        storeName: String,
        storeAddress: String
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, password)
            .addOnCompleteListener { p0 ->
                if (p0.isSuccessful) {
                    val newUserDatabaseRegistration = User(
                        FirebaseAuth.getInstance().currentUser?.uid.toString(),
                        username,
                        storeName,
                        storeAddress
                    )
                    FirebaseDatabase.getInstance().reference
                        .child("Users")
                        .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                        .setValue(newUserDatabaseRegistration).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Üye kaydedildi" + FirebaseAuth.getInstance().currentUser?.uid,
                                    Toast.LENGTH_SHORT
                                ).show()
                                FirebaseAuth.getInstance().signOut()
                                RedirectToLogin()
                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    task.exception.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    //Bilinemeyen nedenlerlen dolayı kayıt gerçekleştirilemedi lütfen internetinizi kontrol edip tekrar deneyiniz

                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Üye kaydedilirken sorun çıktı" + p0.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun RedirectToLogin() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}

