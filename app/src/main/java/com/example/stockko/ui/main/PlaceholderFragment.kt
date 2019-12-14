package com.example.stockko.ui.main

import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.stockko.R
import com.example.stockko.product.ProductItem
import com.example.stockko.product.ProductRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*


class PlaceholderFragment : Fragment() {
    private var allProduct = ArrayList<ProductItem>()
    lateinit var myAdapter: ProductRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        var position = (arguments?.getString(ARG_SECTION_TITLE) ?: 1)

        when (position) {
            "Cute" -> {
                fillDataSource()
            }
            "aysegul" -> {

            }
        }
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerViewItem)
        myAdapter = ProductRecyclerViewAdapter(allProduct)
        recyclerView.adapter = myAdapter

        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager

        return root
    }
    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_SECTION_TITLE = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int, sectionTitle: String): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                    putString(ARG_SECTION_TITLE, sectionTitle)
                }
            }
        }
    }

    fun fillDataSource() {
        var allImages = arrayOf(
            R.drawable.manzara1,
            R.drawable.manzara2,
            R.drawable.manzara3,
            R.drawable.manzara4,
            R.drawable.manzara5,
            R.drawable.manzara6,
            R.drawable.manzara7,
            R.drawable.manzara8,
            R.drawable.manzara9,
            R.drawable.manzara10,
            R.drawable.manzara11,
            R.drawable.manzara12,
            R.drawable.manzara13,
            R.drawable.manzara14,
            R.drawable.manzara15,
            R.drawable.manzara16,
            R.drawable.manzara17,
            R.drawable.manzara18,
            R.drawable.manzara19,
            R.drawable.manzara20
        )
        var animalName = arrayOf(
            "Kedi",
            "Kedi",
            "Köpek",
            "Kedi",
            "Kedi",
            "Kedi",
            "Köpek",
            "res",
            "Kedi",
            "Kedi",
            "at",
            "Kedi",
            "Kedi",
            "Kedi",
            "at",
            "Kedi",
            "at",
            "Kedi",
            "Kedi",
            "Kedi"
        )
        for (i in 0.until(allImages.size)) {
            var addedAnimal = ProductItem(
                animalName[i],
                allImages[i]
            )
            allProduct.add(addedAnimal)
        }
    }

}
