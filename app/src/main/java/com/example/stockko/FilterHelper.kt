package com.example.stockko

import android.widget.Filter
import com.example.stockko.product.ProductItem
import com.example.stockko.product.ProductRecyclerViewAdapter

class FilterHelper(
    private var allProduct: ArrayList<ProductItem>,
    var adapter: ProductRecyclerViewAdapter
) :
    Filter() {

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

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        adapter.setFilter(results?.values as ArrayList<ProductItem>)
        adapter.notifyDataSetChanged()
    }

}