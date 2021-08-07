package com.example.savethefood.fooddetail

import android.view.View
import com.example.savethefood.BaseAdapter
import com.example.savethefood.R
import com.example.savethefood.databinding.PairItemBinding
import com.example.savethefood.shared.data.domain.FoodDomain

class FoodPantryAdapter(
    onClickListener: BaseClickListener<FoodDomain>,
) : BaseAdapter<FoodDomain, PairItemBinding>(
    onClickListener,
    { old, new -> old.id == new.id },
    { old, new -> old == new }
) {

    override val layoutRes: Int
        get() = R.layout.pair_item

    override fun bind(
        holder: BaseViewHolder<PairItemBinding>,
        clickListener: BaseClickListener<FoodDomain>,
        item: FoodDomain
    ) {
        with(dataBinding) {
            foodDomain = item
            foodCallback = clickListener as PantryBaseClickListener?
        }
    }

    class PantryBaseClickListener(
        override val clickListener: (food: FoodDomain) -> Unit,
        val clickViewListener: (recipe: FoodDomain, view: View) -> Unit
    ) : BaseClickListener<FoodDomain>(clickListener) {

        fun onClick(food: FoodDomain, view: View) = clickViewListener(food, view)
    }
}