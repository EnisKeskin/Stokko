package com.example.stockko.product

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.stockko.DetailActivity
import com.example.stockko.R
import com.example.stockko.dataClass.ProductItem
import kotlinx.android.synthetic.main.simple_view.view.*

//Burda recyclerView adapterının yazıldığı yer.
//ProductViewHolder ise nesnemizin alt klass olarak adapter verin veri türüdür
//Filterable ise Filtrelemek istediğimiz verilerin burda olduğundan buraya bir implement edilen bir metodtur
class ProductRecyclerViewAdapter(private var allProduct: ArrayList<ProductItem>) :
    RecyclerView.Adapter<ProductRecyclerViewAdapter.ProductViewHolder>(), Filterable {
    //filter nesnesini tanımlıyoruz böylece FilterHelper kullanabiliyoruz

    var myFilter: FilterHelper = FilterHelper(allProduct, this@ProductRecyclerViewAdapter)

    //Burda hem RecyclerView içerisinde bulunacak itemi hemde metodları tanımlayabiliyoruz her bir nesnede tekrar çalışır
    inner class ProductViewHolder(simpleView: View) : RecyclerView.ViewHolder(simpleView) {
        var simpleItem = simpleView as CardView
        var simpleName = simpleItem.tvProductName
        var simpleImage = simpleItem.imgProduct
        fun setData(productCreatedAtThatMoment: ProductItem, position: Int) {
            simpleName.text = productCreatedAtThatMoment.nameOfTheProduct
           // simpleImage.setImageResource(productCreatedAtThatMoment.productImage)

            simpleItem.setOnClickListener { v ->
                val intent = Intent(v.context, DetailActivity::class.java)
                v.context.startActivity(intent)
            }

        }
    }

    //oluşturduğumuz item yani tek bir satırın oluşturulduğu yer
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        val simpleItem = inflate.inflate(R.layout.simple_view, parent, false)
        return ProductViewHolder(simpleItem)
    }

    //kaç tane item gözükeceği yer
    override fun getItemCount(): Int {
        return allProduct.size
    }

    //hey bir item'in oluştuktan sonraki olaylarının oluştuğu yer bu sayede her bir item'a tek tek ulaşabiliyoruz.
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val productCreatedAtThatMoment = allProduct[position]
        holder.setData(productCreatedAtThatMoment, position)
    }

    //FilterHelper kullandığı metod bu sayede filterelenen veriler geliyor
    fun setFilter(arrayList: ArrayList<ProductItem>) {
        allProduct = arrayList
    }

    //hangi filter kullanıldığının belirtildiği yer
    override fun getFilter(): Filter {
        return myFilter
    }

}