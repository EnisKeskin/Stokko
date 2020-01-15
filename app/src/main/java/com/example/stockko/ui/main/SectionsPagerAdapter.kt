package com.example.stockko.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.stockko.R
import com.example.stockko.dataClass.Category
import com.example.stockko.dataClass.Titles

private val TAB_TITLES = ArrayList<Category>()
private val firstTitle = Category()
//tab işlemlerinin gerçekleştiği yer
@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNCHECKED_CAST")
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {
    init {
        firstTitle.name = context.resources.getString(R.string.tab_text_1)
        firstTitle.key = "0"
        TAB_TITLES.add(firstTitle)
        TAB_TITLES.addAll(Titles.getTitle())
        Titles.getTitle().forEach {
            println(it.name)
        }

    }

    //tab sayfasının başlıklarının yazıldığı yer
    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES[position].name
    }

    //her tab sayfasının altına hangi viewPage ekleneceği yer PlaceHolderFragment yaptığımız kısım
    override fun getItem(position: Int): Fragment {
        println("item girildi")
        //yeni bir instance oluşturuluyor.
        return PlaceholderFragment.newInstance(
            TAB_TITLES[position]
        )
    }

    // Show 2 total pages.
    override fun getCount(): Int {
        return TAB_TITLES.size
    }

}

interface Operation<T> {
    fun performAsync(callback: (T?, Throwable?) -> Unit)
}