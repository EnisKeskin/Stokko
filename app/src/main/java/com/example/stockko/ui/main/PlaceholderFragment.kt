package com.example.stockko.ui.main

import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.stockko.R
import com.example.stockko.product.ProductItem
import com.example.stockko.product.ProductRecyclerViewAdapter

//PlaceholderFragment yani tab ile ilgili iç işlemlerin gerçekleştiği yer
@Suppress("DEPRECATION")
class PlaceholderFragment : Fragment() {
    private var allProduct = ArrayList<ProductItem>()
    lateinit var myAdapter: ProductRecyclerViewAdapter
    //nesnelerin oluştuğu yer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //nesnelerin görüntülerinin oluştuğu yer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //fragment_main yani recyclerView olduğu kısım inflater yani o sayfaya bir dış kaynak olarak eklenir
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        //pozisyonu yani hangi pozisyonda olduğunu örnekmek için örnektir veritabını eklenince daha düzgün kodu yazılacak
        val position = (arguments?.getString(ARG_SECTION_TITLE) ?: 1)
        //sadece verilere ulaşıp ulaşamadığımızın kontrolü
        when (position) {
            "Cute" -> {
                fillDataSource()
            }
            "ayşegül" -> {

            }
        }
        //recyclerView adapterının tanımlanması bu sayede içine simple_view aktarılabilecek
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerViewItem)
        myAdapter = ProductRecyclerViewAdapter(allProduct)
        recyclerView.adapter = myAdapter
        //recyclerView optimizasyonu sağladık
        recyclerView.setHasFixedSize(true)
        //Cache tutacak boyut
        recyclerView.setItemViewCacheSize(20)
        //boyuttan alacağını açtık
        recyclerView.isDrawingCacheEnabled = true
        //Cachelenen cizimleri aldık
        recyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        //recyclerView bir layout yani görüntü kazandırmak için bir çok görüntü bicimi var StaggeredHorizontal ..vb gibi
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
        // bunun sayesinde viewPage icine verilerimiz aktarılıyor.
        return root
    }

    //sectionPagerAdapter newInstance yani yeni bir örnek oluşturulduğu yer bu sayede bu sınıftan oluşturulan her şey
    //tab icerisindeki viewPage içerisinden görüntülebiliyor
    companion object {
        //bulunduğu konumdaki sayi
        private const val ARG_SECTION_NUMBER = "section_number"
        //bulunduğu konumdaki ismi
        private const val ARG_SECTION_TITLE = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int, sectionTitle: String): PlaceholderFragment {
            return PlaceholderFragment().apply {
                //arguments sayesinden instance oluşturulurken oluşturulan yerden tab title ve kaçıncı sırada olduğu bilgisini elde ediyoruz.
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                    putString(ARG_SECTION_TITLE, sectionTitle)
                }
            }
        }
    }

    //örnek verilerin icerisinden barındıran fonksiyon
    fun fillDataSource() {
        var allImages = arrayOf(
            R.drawable.manzara1,
            R.drawable.manzara2,
            R.drawable.manzara3,
            R.drawable.manzara4,
            R.drawable.manzara5,
            R.drawable.manzara6,
            R.drawable.manzara7,
            R.drawable.manzara8,
            R.drawable.manzara9,
            R.drawable.manzara10,
            R.drawable.manzara11,
            R.drawable.manzara12,
            R.drawable.manzara13,
            R.drawable.manzara14,
            R.drawable.manzara15,
            R.drawable.manzara16,
            R.drawable.manzara17,
            R.drawable.manzara18,
            R.drawable.manzara19,
            R.drawable.manzara20
        )
        var animalName = arrayOf(
            "Kedi",
            "Kedi",
            "Köpek",
            "Kedi",
            "Kedi",
            "Kedi",
            "Köpek",
            "res",
            "Kedi",
            "Kedi",
            "at",
            "Kedi",
            "Kedi",
            "Kedi",
            "at",
            "Kedi",
            "at",
            "Kedi",
            "Kedi",
            "Kedi"
        )
        for (i in 0.until(allImages.size)) {
            var addedAnimal = ProductItem(
                animalName[i],
                allImages[i]
            )
            allProduct.add(addedAnimal)
        }
    }

}
