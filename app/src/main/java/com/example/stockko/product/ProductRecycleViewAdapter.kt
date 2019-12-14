package com.example.stockko.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.stockko.FilterHelper
import com.example.stockko.R
import kotlinx.android.synthetic.main.simple_view.view.*

class ProductRecyclerViewAdapter(private var allProduct: ArrayList<ProductItem>) :
    RecyclerView.Adapter<ProductRecyclerViewAdapter.ProductViewHolder>(), Filterable {

    var myFilter: FilterHelper = FilterHelper(allProduct, this)

    inner class ProductViewHolder(simpleView: View) : RecyclerView.ViewHolder(simpleView) {
        var simpleItem = simpleView as CardView
        var simpleName = simpleItem.tvProductName
        var simpleImage = simpleItem.imgProduct

        fun setData(animalCreatedAtThatMoment: ProductItem, position: Int) {
            simpleName.text = animalCreatedAtThatMoment.nameOfTheProduct
            simpleImage.setImageResource(animalCreatedAtThatMoment.productImage)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        val simpleItem = inflate.inflate(R.layout.simple_view, parent, false)
        return ProductViewHolder(simpleItem)
    }

    override fun getItemCount(): Int {
        return allProduct.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val productCreatedAtThatMoment = allProduct[position]
        holder.setData(productCreatedAtThatMoment, position)
    }

    fun setFilter(arrayList: ArrayList<ProductItem>) {
        allProduct = arrayList
    }

    override fun getFilter(): Filter {
        return myFilter
    }


}