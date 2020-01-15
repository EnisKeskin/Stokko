package com.example.stockko.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.stockko.R
import com.example.stockko.dataClass.Category
import com.example.stockko.dataClass.Product
import com.example.stockko.dataClass.ProductItem
import com.example.stockko.dataClass.Products
import com.example.stockko.product.ProductRecyclerViewAdapter
import com.google.firebase.database.FirebaseDatabase


//PlaceholderFragment yani tab ile ilgili iç işlemlerin gerçekleştiği yer
@Suppress("DEPRECATION")
class PlaceholderFragment : Fragment() {
    private var allProduct = ArrayList<Products>()
    lateinit var myAdapter: ProductRecyclerViewAdapter
    //nesnelerin oluştuğu yer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = ProductRecyclerViewAdapter(allProduct)
        //myAdapter.notifyDataSetChanged()
        // myAdapter.setHasStableIds(true)
    }

    //nesnelerin görüntülerinin oluştuğu yer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //fragment_main yani recyclerView olduğu kısım inflater yani o sayfaya bir dış kaynak olarak eklenir
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        //recyclerView adapterının tanımlanması bu sayede içine simple_view aktarılabilecek
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerViewItem)
        recyclerView.adapter = myAdapter
        println("Rec oldu")
        //recyclerView optimizasyonu sağlamak için fixedsize yani boyutunu sabit tutmayı aktif hale getirdik
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
        //bulunduğu konumdaki ismi
        private const val ARG_SECTION_TITLE = "section_title"

        private const val ARG_SECTION_TITLE_ID = "section_title"

        @JvmStatic
        fun newInstance(sectionTitle: Category): PlaceholderFragment {
            return PlaceholderFragment().apply {
                //Arguments sayesinden instance oluşturulurken oluşturulan yerden
                // tab title ve kaçıncı sırada olduğu bilgisini elde ediyoruz.
                arguments = Bundle().apply {
                    putString(ARG_SECTION_TITLE, sectionTitle.name)
                }
            }
        }
    }

    fun allProduct(){
        val reference = FirebaseDatabase.getInstance().reference
    }

}
