package com.example.savethefood.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.shared.data.domain.BagDomain
import com.example.savethefood.shopping.BagAdapter

object ShoppingBindingUtils {

    @JvmStatic
    @BindingAdapter("bind:listData")
    fun bindBagRecycleView(recyclerView: RecyclerView, data: List<BagDomain>?) {
        val adapter = recyclerView.adapter as BagAdapter
        adapter.submitList(data)
        recyclerView.scheduleLayoutAnimation()
    }
}
