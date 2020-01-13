package com.example.stockko

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        btnKaydol.setOnClickListener {
            if(etMail.text.isNotEmpty() && etSifre.text.isNotEmpty() && etSifreTekrar.text.isNotEmpty() && etAd.text.isNotEmpty() && etMagazaAdi.text.isNotEmpty() && etMagazaAdres.text.isNotEmpty()) {
                if(etSifre.text.toString().equals(etSifreTekrar.text.toString())){

                    yeniUyeKayit(etMail.text.toString(),etSifre.text.toString())
                    var intent2 = Intent(this@RegisterActivity,LoginActivity::class.java)
                    startActivity(intent2)
                }
                else{
                    Toast.makeText(this,"Şifreler aynı değil",Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this,"Boş alanları doldurunuz",Toast.LENGTH_SHORT).show()
            }
            }
        }

    private fun yeniUyeKayit(mail: String, sifre: String) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail,sifre)
            .addOnCompleteListener(object:OnCompleteListener<AuthResult>{
                override fun onComplete(p0: Task<AuthResult>) {

                    if (p0.isSuccessful) {

                        Toast.makeText(this@RegisterActivity, "Üye kaydedildi"+FirebaseAuth.getInstance().currentUser?.uid, Toast.LENGTH_SHORT).show()
                        FirebaseAuth.getInstance().signOut()

                    }

                    else{
                        Toast.makeText(this@RegisterActivity,"Üye kaydedilirken sorun çıktı"+p0.exception?.message,Toast.LENGTH_SHORT).show()
                    }
                }

            })

    }

}

