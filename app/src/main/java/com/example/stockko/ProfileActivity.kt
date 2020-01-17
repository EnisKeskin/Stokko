package com.example.stockko

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.stockko.dataClass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.etAddress
import kotlinx.android.synthetic.main.activity_register.etPassword
import kotlinx.android.synthetic.main.activity_register.etShopName
import kotlinx.android.synthetic.main.activity_register.etUserName

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        btnUpdate.setOnClickListener {
            if (etUserName.text.isNotEmpty() && etMail.text.isNotEmpty() && etPassword.text.isNotEmpty() && etPasswordAgain.text.isNotEmpty() && etUserName.text.isNotEmpty() && etShopName.text.isNotEmpty() && etAddress.text.isNotEmpty()) {
                    newUserRegistration(
                        etMail.text.toString(),
                        etPassword.text.toString(),
                        etUserName.text.toString(),
                        etShopName.text.toString(),
                        etAddress.text.toString()
                    )
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
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
                                    this,
                                    "Üye bilgileri güncellendi" + FirebaseAuth.getInstance().currentUser?.uid,
                                    Toast.LENGTH_SHORT
                                ).show()
                                FirebaseAuth.getInstance()
                            } else {
                                Toast.makeText(
                                    this,
                                    task.exception.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    //Bilinemeyen nedenlerlen dolayı kayıt gerçekleştirilemedi lütfen internetinizi kontrol edip tekrar deneyiniz

                } else {
                    Toast.makeText(
                        this,
                        "Üye güncellenirken sorun çıktı" + p0.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}

