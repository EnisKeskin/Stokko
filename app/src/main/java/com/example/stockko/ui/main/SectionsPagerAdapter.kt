package com.example.stockko.ui.main

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.stockko.R

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3,
    R.string.tab_text_4
)

//tab işlemlerinin gerçekleştiği yer
@Suppress("DEPRECATION")
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {
    init {
        println("çalıştı")
    }
    //tab sayfasının başlıklarının yazıldığı yer
    //context.resources.getString alamasının nedeni Tab_Title bir strings icinde tanımlı tab_text_1,tab_text_2 gibi veriler olması
    //veritabanında tanımlarken herhangi bir sıkıntı olmayacak.
    override fun getPageTitle(position: Int): CharSequence? {
        //title girileceği yer
        println("title girildi")
        return context.resources.getString(TAB_TITLES[position])
    }

    //her tab sayfasının altına hangi viewPage ekleneceği yer PlaceHolderFragment yaptığımız kısım
    override fun getItem(position: Int): Fragment {
        println("item girildi")
        //yeni bir instance oluşturuluyor.
        return PlaceholderFragment.newInstance(
            position,
            context.resources.getString(
                TAB_TITLES[position]
            )
        )
    }

    // Show 2 total pages.
    override fun getCount(): Int {
        return TAB_TITLES.size
    }

}