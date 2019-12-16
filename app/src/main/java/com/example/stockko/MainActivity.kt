package com.example.stockko

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.stockko.product.ProductRecyclerViewAdapter
import com.example.stockko.ui.main.SectionsPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Integer.max

class MainActivity() : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var viewPager: ViewPager

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //SectionsPagerAdapter oluşturulduğu kısım oluşturulduğu yer
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager = findViewById(R.id.view_pager)
        //adapyerının tanımlandığı kısım
        viewPager.adapter = sectionsPagerAdapter
        //tablara viewPage eklendiği kısım

        val tabLayout: TabLayout = findViewById(R.id.tabs)
        //tabLayoutları boyutunu tam ekrana göre ayarlayan çoğaldıkca boyutu ona göre ayarlayıp düzenli bir görüntü oluşturuyor

        tabLayout.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            //toplam boyutu tanımlanıyor
            var totalWidth = 0
            //maksimim boyutu tanımlanıyor
            var maxWidth = 0
            //tüm tabların sayısı kadar geziyor
            for (i in 0 until tabLayout.tabCount) {
                //tabların boyutunu alıyor
                val tabWidth = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)!!.width
                //toplam boyututa ekliyor
                totalWidth += tabWidth
                //max sayıyı maxWidth atıyor böylece hepsini eşit yapabilir eğer ekrandan küçükse
                maxWidth = max(maxWidth, tabWidth)
            }
            //ekranın boyutunu alıyor
            val screenWidth = Resources.getSystem().displayMetrics.widthPixels

            if (totalWidth < screenWidth && screenWidth / tabLayout.tabCount >= maxWidth) {

                tabLayout.tabMode = TabLayout.MODE_FIXED
            }
        }

        tabLayout.setupWithViewPager(viewPager)

        tabLayout.viewTreeObserver.addOnGlobalFocusChangeListener { oldFocus, newFocus ->
            val floatButton: FloatingActionButton = btnBarkod
            if (newFocus != null) {
                floatButton.visibility = (View.GONE)
            } else {
                Handler().postDelayed({
                    floatButton.visibility = (View.VISIBLE)
                }, 300)

            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Toolbar searcView eklendiği kısım
        menuInflater.inflate(R.menu.menu, menu)
        val searchItem = menu?.findItem(R.id.searchProduct)
        val searchView = searchItem?.actionView as SearchView
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


