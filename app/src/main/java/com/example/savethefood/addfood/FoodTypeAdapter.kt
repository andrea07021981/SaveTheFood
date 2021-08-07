package com.example.savethefood.addfood

import com.example.savethefood.BaseAdapter
import com.example.savethefood.R
import com.example.savethefood.databinding.FoodTypeItemBinding
import com.example.savethefood.shared.data.domain.FoodItem
import com.example.savethefood.shared.data.domain.*

class FoodTypeAdapter(
    onClickListener: BaseClickListener<FoodItem>,
) : BaseAdapter<FoodItem, FoodTypeItemBinding>(
    onClickListener,
    { old, new -> old.name == new.name },
    { old, new -> old == new }
) {

    override val layoutRes: Int
        get() = R.layout.food_type_item

    override fun bind(
        holder: BaseViewHolder<FoodTypeItemBinding>,
        clickListener: BaseClickListener<FoodItem>,
        item: FoodItem
    ) {
        with(dataBinding) {
            foodItem = item
            foodCallback = clickListener
        }
    }
}