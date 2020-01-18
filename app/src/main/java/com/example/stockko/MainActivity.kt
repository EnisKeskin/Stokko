package com.example.stockko

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.stockko.dataClass.SectionsPagerGlobal
import com.example.stockko.product.ProductRecyclerViewAdapter
import com.example.stockko.scandit.BarcodeScanActivity
import com.example.stockko.ui.main.SectionsPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Integer.max


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    NavigationView.OnNavigationItemSelectedListener {

    lateinit var mydialog: Dialog
    private lateinit var viewPager: ViewPager
    lateinit var myadapter: ProductRecyclerViewAdapter
    lateinit var tabLayout: TabLayout
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var toolbar: Toolbar

    @SuppressLint("RestrictedApi", "InflateParams")
    @RequiresApi(Build.VERSION_CODES.N)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        var inflater = LayoutInflater.from(this)
        //SectionsPagerAdapter oluşturulduğu kısım oluşturulduğu yer
        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        SectionsPagerGlobal.setSectionPager(sectionsPagerAdapter)
        viewPager = findViewById(R.id.view_pager)
        //adapyerının tanımlandığı kısım
        viewPager.adapter = sectionsPagerAdapter
        //tablara viewPage eklendiği kısım
        tabLayout = findViewById(R.id.tabs)
        //tabLayoutları boyutunu tam ekrana göre ayarlayan çoğaldıkca boyutu ona göre ayarlayıp düzenli bir görüntü oluşturuyor
        tabLayout.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
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
        //tıkladığım tabloların eski ve yeni halini bana veriyor bu sayade verileri istediğim yere yazdırabiliyorum.
        tabLayout.viewTreeObserver.addOnGlobalFocusChangeListener { _, newFocus ->
            val floatButton: FloatingActionButton = btnBarkod
            if (newFocus != null) {
                floatButton.visibility = (View.GONE)
                //searcview için yenibir SectionsPagerAdapter yazarak hali hazırda olan recyclerView kullanabiliyorum.
                val sectionsPagerAdapter =
                    com.example.stockko.search.SectionsPagerAdapter(
                        this,
                        supportFragmentManager
                    )
                //search recyclerView adapterıma ekleniyor
                viewPager.adapter = sectionsPagerAdapter
                //
                val res = inflater.inflate(R.layout.fragment_main, null)

                //hali hazırda bulunan fragment_main içine yerleştiriyorum
                mainConstraintLayout.addView(res)


            } else {
                viewPager.adapter = sectionsPagerAdapter
                //SectionsPagerAdapter oluşturulduğu kısım oluşturulduğu yer
                Handler().postDelayed({
                    floatButton.visibility = (View.VISIBLE)
                }, 300)

            }

        }

        mydialog = Dialog(this)

        btnBarkod.setOnClickListener {
            val intent = Intent(this, BarcodeScanActivity::class.java)
            startActivity(intent)
        }

    }

    override fun getClassLoader(): ClassLoader {
        println("main yüklendi")
        return super.getClassLoader()
    }

    //menunun oluşturulduğu yer.
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
        myadapter =
            viewPager.findViewById<RecyclerView>(R.id.recyclerViewItem).adapter as ProductRecyclerViewAdapter
        myadapter.myFilter.filter(newText)
        return true
    }

    fun SnowPopup(view: View) {
        val kaydet: Button
        mydialog.setContentView(R.layout.custompopup)
        kaydet = mydialog.findViewById(R.id.btnProductAdd)
        mydialog.show()
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.nav_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_product -> {
                val intent = Intent(this, ProductActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_category -> {
                val intent = Intent(this, CategoryActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_update -> {
                Toast.makeText(this, "Update clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}



