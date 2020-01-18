package com.example.stockko.dataClass

import com.example.stockko.ui.main.SectionsPagerAdapter

object SectionsPagerGlobal {

    var sectionPager: ArrayList<SectionsPagerAdapter> = ArrayList()

    fun setSectionPager(sec: SectionsPagerAdapter) {
        this.sectionPager.clear()
        this.sectionPager.add(sec)
    }

    fun getSectionPager(): SectionsPagerAdapter {
        return this.sectionPager[0]
    }


}