package com.example.stockko

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.stockko.dataClass.Category
import com.example.stockko.dataClass.SectionsPagerGlobal
import com.example.stockko.dataClass.Titles
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_category.*

@Suppress("NAME_SHADOWING")
@SuppressLint("Registered")
class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun btnCategoryAdd(v: View) {
        val category = tvCategory1.text.toString()
        if (category.isNotEmpty()) {
            val reference = FirebaseDatabase.getInstance().reference
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val categoryDatabase =
                reference.child("Product").child(userId.toString()).child("category")
            categoryDatabase.push().child("name").setValue(category).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val refarence = FirebaseDatabase.getInstance().reference
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    println(userId)
                    val query = refarence.child("Product")
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
                                println("Başarılı")
                                SectionsPagerGlobal.getSectionPager().notifyDataSetChanged()
                            }
                        }
                    })
                } else {
                    println("Başarısız")
                }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        goToMain()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        goToMain()
        super.onBackPressed()
    }
    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}