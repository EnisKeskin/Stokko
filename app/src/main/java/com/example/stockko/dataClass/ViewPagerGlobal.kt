package com.example.stockko.dataClass

import com.example.stockko.product.ProductRecyclerViewAdapter


object ViewPagerGlobal {

    //viewPager.findViewById<RecyclerView>(R.id.recyclerViewItem).adapter as ProductRecyclerViewAdapter
    var productRecycler: ArrayList<ProductRecyclerViewAdapter> = ArrayList()

    fun setSectionPager(sec: ProductRecyclerViewAdapter) {
        this.productRecycler.clear()
        this.productRecycler.add(sec)
    }

    fun getSectionPager(): ProductRecyclerViewAdapter {
        return this.productRecycler[0]
    }
}