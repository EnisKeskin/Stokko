package com.example.stockko

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.stockko.product.ProductItem
import com.example.stockko.product.ProductRecyclerViewAdapter
import com.example.stockko.ui.main.PlaceholderFragment
import com.example.stockko.ui.main.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.simple_view.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        var searchItem = menu?.findItem(R.id.searchProduct)
        var searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val recyclerView: RecyclerView = viewPager.findViewById(R.id.recyclerViewItem)
        val myadapter = recyclerView.adapter as ProductRecyclerViewAdapter
        myadapter.myFilter.filter(newText)
        return true
    }

}


