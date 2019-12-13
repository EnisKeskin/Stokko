package com.example.stockko.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.stockko.R
import kotlinx.android.synthetic.main.simple_view.view.*

class ProductRecyclerViewAdapter(allProduct: ArrayList<ProductItem>) :
    RecyclerView.Adapter<ProductRecyclerViewAdapter.ProductViewHolder>() {

    var allProduct = allProduct

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
        var inflate = LayoutInflater.from(parent.context)
        var simpleItem = inflate.inflate(R.layout.simple_view, parent, false)
        return ProductViewHolder(simpleItem)
    }

    override fun getItemCount(): Int {
        return allProduct.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        var productCreatedAtThatMoment = allProduct[position]
        holder.setData(productCreatedAtThatMoment, position)
    }


}