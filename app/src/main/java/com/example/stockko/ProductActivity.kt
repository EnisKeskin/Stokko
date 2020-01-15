package com.example.stockko

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stockko.dataClass.Titles

@SuppressLint("Registered")
class ProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_page)
        var ct = Titles.getTitle()
        ct.forEach {
            println(it.name)
        }
    }


}