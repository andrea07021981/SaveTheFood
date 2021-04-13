package com.example.savethefood.home

import com.example.savethefood.BaseAdapter
import com.example.savethefood.R
import com.example.savethefood.databinding.FoodItemBinding
import com.example.savethefood.data.domain.FoodDomain

class FoodAdapter(
    onClickListener: BaseClickListener<FoodDomain>,
) : BaseAdapter<FoodDomain, FoodItemBinding>(onClickListener) {

    override val layoutRes: Int
        get() = R.layout.food_item

    override fun bind(
        holder: BaseViewHolder<FoodItemBinding>,
        clickListener: BaseClickListener<FoodDomain>,
        item: FoodDomain
    ) {
        with(dataBinding) {
            foodDomain = item
            foodCallback = clickListener
        }
    }
}