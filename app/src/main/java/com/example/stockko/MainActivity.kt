package com.example.stockko

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.stockko.product.ProductItem
import com.example.stockko.product.ProductRecyclerViewAdapter
import com.example.stockko.ui.main.SectionsPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.lang.Integer.max


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    lateinit var mydialog: Dialog
    private lateinit var viewPager: ViewPager
    lateinit var myadapter: ProductRecyclerViewAdapter
    lateinit var tabLayout: TabLayout
    var tabsPosition: Int = 0
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    @SuppressLint("RestrictedApi", "InflateParams")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")
        var inflater = LayoutInflater.from(this)

        //SectionsPagerAdapter oluşturulduğu kısım oluşturulduğu yer
        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager = findViewById(R.id.view_pager)
        //adapyerının tanımlandığı kısım
        viewPager.adapter = sectionsPagerAdapter
        //tablara viewPage eklendiği kısım
        tabLayout = findViewById(R.id.tabs)
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
        //tıkladığım tabloların eski ve yeni halini bana veriyor bu sayade verileri istediğim yere yazdırabiliyorum.
        tabLayout.viewTreeObserver.addOnGlobalFocusChangeListener { oldFocus, newFocus ->
            val floatButton: FloatingActionButton = btnBarkod
            if (newFocus != null) {
                floatButton.visibility = (View.GONE)
                mainConstraintLayout.appBarLayout.visibility = View.GONE
                //searcview için yenibir SectionsPagerAdapter yazarak hali hazırda olan recyclerView kullanabiliyorum.
                var sectionsPagerAdapter =
                    com.example.stockko.Search.SectionsPagerAdapter(
                        this,
                        supportFragmentManager
                    )
                //search recyclerView adapterıma ekleniyor
                viewPager.adapter = sectionsPagerAdapter
                //
                var res = inflater.inflate(R.layout.fragment_main, null)

                //hali hazırda bulunan fragment_main içine yerleştiriyorum
                mainConstraintLayout.addView(res)


            } else {
                mainConstraintLayout.appBarLayout.visibility = View.VISIBLE
                viewPager.adapter = sectionsPagerAdapter
                //SectionsPagerAdapter oluşturulduğu kısım oluşturulduğu yer
                Handler().postDelayed({
                    floatButton.visibility = (View.VISIBLE)
                }, 300)

            }

        }
        mydialog = Dialog(this)

    }

    fun SnowPopup(v : View){
        val kaydet: Button
        mydialog.setContentView(R.layout.custompopup)
        kaydet =  mydialog.findViewById(R.id.kaydet)
        mydialog.show()

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        println("reisssssssssss")
        return super.onOptionsItemSelected(item)
    }

    //false olasının nedeni searcView gönderildiğinde aramayapmasını değil.
    //kelime veya harf bazlı direk yazınca aramasını istediğimizden
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    //Text herhangi bir harf yazıldığında gerçekleşen kısım
    override fun onQueryTextChange(newText: String?): Boolean {
        var text = newText?.toLowerCase()
        var searchList = ArrayList<ProductItem>()
        //filterelemeyi recycler isteğimiz için önce recyclerView alıyoruz
        // myadapter = viewPager.findViewById<RecyclerView>(R.id.recyclerViewItem).adapter as ProductRecyclerViewAdapter

        //icindeki adapterdan myfilter ulaşarak yazılan yazıyı gönderiyoruz.
        var tabs = findViewById<TabLayout>(R.id.tabs)

        myadapter =
            viewPager.findViewById<RecyclerView>(R.id.recyclerViewItem).adapter as ProductRecyclerViewAdapter
        Log.e("dış katman", "$myadapter")
        myadapter.myFilter.filter(newText)
        return true
    }

}



