package com.example.savethefood.food

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.BaseAdapter
import com.example.savethefood.R
import com.example.savethefood.data.domain.FoodItem
import com.example.savethefood.data.domain.FoodSearchDomain
import com.example.savethefood.data.domain.ProductDomain
import com.example.savethefood.databinding.FoodSearchItemBinding
import com.example.savethefood.databinding.FoodTypeItemBinding

class FoodSearchAdapter(
    onClickListener: BaseClickListener<ProductDomain>,
) : BaseAdapter<ProductDomain, FoodSearchItemBinding>(onClickListener) {

    override val layoutRes: Int
        get() = R.layout.food_search_item

    override fun bind(
        holder: BaseViewHolder<FoodSearchItemBinding>,
        clickListener: BaseClickListener<ProductDomain>,
        item: ProductDomain
    ) {
        with(dataBinding) {
            productItem = item
            searchFoodCallback = clickListener
        }
    }
}