package com.example.stockko.product

import android.annotation.SuppressLint
import android.widget.Filter

@Suppress("UNCHECKED_CAST")
class FilterHelper(
    private var allProduct: ArrayList<ProductItem>,
    var adapter: ProductRecyclerViewAdapter
    ) :
    Filter() {
    //Filtrelemenin yapıldığı yer burda filtreleme şartlarını belirterek direkt olarak veri işlenir.
    @SuppressLint("DefaultLocale")
    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var result = FilterResults()

        if (constraint != null && constraint.isNotEmpty()) {
            var wantedName = constraint.toString().toLowerCase()
            var matched = ArrayList<ProductItem>()

            for (dost in allProduct) {
                var name = dost.nameOfTheProduct.toLowerCase()
                if (name.contains(wantedName)) {
                    matched.add(dost)
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
        adapter.setFilter(results?.values as ArrayList<ProductItem>)
        adapter.notifyDataSetChanged()
    }

}