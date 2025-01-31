package com.example.stockko.product

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.stockko.DetailActivity
import com.example.stockko.R
import com.example.stockko.dataClass.Products
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.simple_view.view.*

//Burda recyclerView adapterının yazıldığı yer.
//ProductViewHolder ise nesnemizin alt klass olarak adapter verin veri türüdür
//Filterable ise Filtrelemek istediğimiz verilerin burda olduğundan buraya bir implement edilen bir metodtur
class ProductRecyclerViewAdapter(private var allProduct: ArrayList<Products>) :
    RecyclerView.Adapter<ProductRecyclerViewAdapter.ProductViewHolder>(), Filterable {
    //filter nesnesini tanımlıyoruz böylece FilterHelper kullanabiliyoruz

    var myFilter: FilterHelper = FilterHelper(allProduct, this@ProductRecyclerViewAdapter)

    //Burda hem RecyclerView içerisinde bulunacak itemi hemde metodları tanımlayabiliyoruz her bir nesnede tekrar çalışır
    inner class ProductViewHolder(simpleView: View) : RecyclerView.ViewHolder(simpleView) {
        var simpleItem = simpleView as CardView
        var simpleName = simpleItem.tvProductName
        var simplebarcodeId = simpleItem.tvProductBarcode
        var simplePiece = simpleItem.tvProductNumber
        var simpleImage = simpleItem.imgProduct
        @SuppressLint("SetTextI18n")
        fun setData(productCreatedAtThatMoment: Products) {
            simpleName.text = "name: " + productCreatedAtThatMoment.name
            simplePiece.text = "piece: " + productCreatedAtThatMoment.piece
            simplebarcodeId.text = "barcode: " + productCreatedAtThatMoment.key
            if (productCreatedAtThatMoment.image != "") {
                val storageRef = FirebaseStorage.getInstance().reference

                val pathReference =
                    storageRef.child("images").child(productCreatedAtThatMoment.image.toString())
                val oneMegabyte: Long = 1024 * 1024
                pathReference.getBytes(oneMegabyte).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                    simpleImage.setImageBitmap(bitmap)
                }.addOnFailureListener {
                    // Handle any errors
                }
            }

            simpleItem.setOnClickListener { v ->
                val intent = Intent(v.context, DetailActivity::class.java)
                intent.putExtra("productKey", productCreatedAtThatMoment.key)
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
        holder.setData(productCreatedAtThatMoment)
    }

    //FilterHelper kullandığı metod bu sayede filterelenen veriler geliyor
    fun setFilter(arrayList: ArrayList<Products>) {
        allProduct = arrayList
    }

    //hangi filter kullanıldığının belirtildiği yer
    override fun getFilter(): Filter {
        return myFilter
    }

}