package com.example.stockko.search

import com.example.stockko.ui.main.PlaceholderFragment
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.stockko.R
import com.example.stockko.dataClass.Category

private val TAB_TITLES = Category()

//tab işlemlerinin gerçekleştiği yer
@Suppress("DEPRECATION")
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm) {
    init {
        TAB_TITLES.id = 0
        TAB_TITLES.name = context.resources.getString(R.string.search)
    }
    //tab sayfasının başlıklarının yazıldığı yer
    //context.resources.getString alamasının nedeni Tab_Title bir strings icinde tanımlı tab_text_1,tab_text_2 gibi veriler olması
    //veritabanında tanımlarken herhangi bir sıkıntı olmayacak.
    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES.name
    }

    //her tab sayfasının altına hangi viewPage ekleneceği yer PlaceHolderFragment yaptığımız kısım
    override fun getItem(position: Int): Fragment {
        //yeni bir instance oluşturuluyor.
        return PlaceholderFragment.newInstance(
            TAB_TITLES
        )
    }

    // Show 2 total pages.
    override fun getCount(): Int {
        return 1
    }

}