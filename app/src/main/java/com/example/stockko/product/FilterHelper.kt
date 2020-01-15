package com.example.stockko.product

import android.annotation.SuppressLint
import android.widget.Filter
import com.example.stockko.dataClass.Product
import com.example.stockko.dataClass.ProductItem
import com.example.stockko.dataClass.Products

@Suppress("UNCHECKED_CAST")
class FilterHelper(
    private var allProduct: ArrayList<Products>,
    var adapter: ProductRecyclerViewAdapter
) :
    Filter() {
    //Filtrelemenin yapıldığı yer burda filtreleme şartlarını belirterek direkt olarak veri işlenir.
    @SuppressLint("DefaultLocale")
    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val result = FilterResults()

        if (constraint != null && constraint.isNotEmpty()) {
            val wantedName = constraint.toString().toLowerCase()
            val matched = ArrayList<Products>()

            for (product in allProduct) {
                val name = product.name
                if (name!!.contains(wantedName)) {
                    matched.add(product)
                }
            }
            result.values = matched
            result.count = matched.size
        } else {
            result.count = allProduct.size
            result.values = allProduct
        }

        return result
    }

    //işlenen veri burda adapter gönderilir setFilter ProductRecyclerViewAdapter bulunan bir fonksiyondur.
    //notifyDataSetChanged ise adapter değiştiğine dair bilgi verir.
    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        adapter.setFilter(results?.values as ArrayList<Products>)
        adapter.notifyDataSetChanged()
    }

}