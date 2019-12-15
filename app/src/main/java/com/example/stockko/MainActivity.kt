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
        //SectionsPagerAdapter oluşturulduğu kısım oluşturulduğu yer
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager = findViewById(R.id.view_pager)
        //adapyerının tanımlandığı kısım
        viewPager.adapter = sectionsPagerAdapter
        //tablara viewPage eklendiği kısım
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Toolbar searcView eklendiği kısım
        menuInflater.inflate(R.menu.menu, menu)
        var searchItem = menu?.findItem(R.id.searchProduct)
        var searchView = searchItem?.actionView as SearchView
        //searcView text değişiklik olduğunda tektiklenen kısım
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }
    //false olasının nedeni searcView gönderildiğinde aramayapmasını değil.
    //kelime veya harf bazlı direk yazınca aramasını istediğimizden
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }
    //Text herhangi bir harf yazıldığında gerçekleşen kısım
    override fun onQueryTextChange(newText: String?): Boolean {
        //filterelemeyi recycler isteğimiz için önce recyclerView alıyoruz
        val recyclerView: RecyclerView = viewPager.findViewById(R.id.recyclerViewItem)
        //icindeki adapterdan myfilter ulaşarak yazılan yazıyı gönderiyoruz.
        val myadapter = recyclerView.adapter as ProductRecyclerViewAdapter
        myadapter.myFilter.filter(newText)
        return true
    }

}


