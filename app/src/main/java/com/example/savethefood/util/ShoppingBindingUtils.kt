package com.example.savethefood.util

import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.shared.data.domain.BagDomain
import com.example.savethefood.shared.data.domain.CategoryItem
import com.example.savethefood.shopping.BagAdapter

object ShoppingBindingUtils {

    @JvmStatic
    @BindingAdapter("bind:listData")
    fun bindBagRecycleView(recyclerView: RecyclerView, data: List<BagDomain>?) {
        val adapter = recyclerView.adapter as BagAdapter
        adapter.submitList(data)
        recyclerView.scheduleLayoutAnimation()
    }

    @JvmStatic
    @BindingAdapter("bind:categoryEntries")
    fun bindCategoryEntries(spinner: Spinner, data: LinkedHashSet<CategoryItem>) {
        // TODO For now the items have only the text
        ArrayAdapter(
            spinner.context,
            android.R.layout.simple_spinner_item,
            data.map { it.name }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }.also {
            spinner.adapter = it
        }
    }
}
