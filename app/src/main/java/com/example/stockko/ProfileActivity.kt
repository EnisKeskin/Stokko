package com.example.stockko

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.stockko.dataClass.Products
import com.example.stockko.dataClass.Titles
import com.example.stockko.dataClass.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.time.LocalDateTime

class ProfileActivity : AppCompatActivity() {
    var kullanici = FirebaseAuth.getInstance().currentUser!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        btnUpdate.setOnClickListener {

            if (etUserName.text.isNotEmpty() && etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty() && etShopName.text.isNotEmpty() && etAddress.text.isNotEmpty()) {

                var credential = EmailAuthProvider.getCredential(
                    kullanici.email.toString(),
                    etPassword.text.toString()
                )
                kullanici.reauthenticate(credential)
                    .addOnCompleteListener { task ->

                        updatePasswordAndMail()

                    }

                val usertValue = User()

                usertValue.username = etUserName.text.toString()
                usertValue.storeAddress = etAddress.text.toString()
                usertValue.storeName = etShopName.text.toString()


                val reference = FirebaseDatabase.getInstance().reference
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val product = reference
                    .child("User")
                    .child(userId.toString())
                    .child("user")

                product.setValue(usertValue)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                FirebaseAuth.getInstance().signOut()
            }

            else {
            Toast.makeText(this, "Güncelleme için lütfen boş alanları doldurunuz.", Toast.LENGTH_SHORT).show()
            }




        }
        val reference = FirebaseDatabase.getInstance().reference
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val mail = FirebaseAuth.getInstance().currentUser?.email
        val user = reference
            .child("Users")
            .child(userId.toString())

        user.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //hata olduğundan main dönecek
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            //image ve category eklenecek
            override fun onDataChange(p0: DataSnapshot) {
                val value = p0.getValue(User::class.java)!!

                etUserName.setText(value.username)
                etShopName.setText(value.storeName)
                etAddress.setText(value.storeAddress)
                etEmail.setText(mail)

            }
        })
    }

    private fun updatePasswordAndMail() {


        if (kullanici != null) {

            kullanici.updatePassword(etPassword.text.toString())
                .addOnCompleteListener { task ->
                    Toast.makeText(this, "Kullanıcı profili güncellendi.", Toast.LENGTH_SHORT).show()
                }
            kullanici.updateEmail(etEmail.text.toString())
                .addOnCompleteListener { task ->
                    Toast.makeText(this, "Kullanıcı profili güncellendi.", Toast.LENGTH_SHORT).show()

                }
        }


    }
}

