package com.example.stockko.dataClass

object Titles {

    var titleArray: ArrayList<Category> = ArrayList()

    fun setTitle(titles: Category) {
        titleArray.add(titles)
    }

    fun getTitle(): ArrayList<Category> {
        return titleArray
    }

    fun clearTitle(){
        titleArray.clear()
    }

}