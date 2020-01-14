package com.example.stockko.search

import com.example.stockko.ui.main.PlaceholderFragment
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.stockko.R

private val TAB_TITLES = arrayOf(
    R.string.search
)

//tab işlemlerinin gerçekleştiği yer
@Suppress("DEPRECATION")
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm) {

    //tab sayfasının başlıklarının yazıldığı yer
    //context.resources.getString alamasının nedeni Tab_Title bir strings icinde tanımlı tab_text_1,tab_text_2 gibi veriler olması
    //veritabanında tanımlarken herhangi bir sıkıntı olmayacak.
    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    //her tab sayfasının altına hangi viewPage ekleneceği yer PlaceHolderFragment yaptığımız kısım
    override fun getItem(position: Int): Fragment {
        //yeni bir instance oluşturuluyor.
        return PlaceholderFragment.newInstance(
            context.resources.getString(
                TAB_TITLES[position]
            ),0
        )
    }

    // Show 2 total pages.
    override fun getCount(): Int {
        return TAB_TITLES.size
    }

}