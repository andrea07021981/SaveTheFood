package com.example.savethefood.recipedetail

import com.example.savethefood.BaseAdapter
import com.example.savethefood.R
import com.example.savethefood.databinding.IngredientItemBinding
import com.example.savethefood.shared.data.domain.ExtendedIngredientDomain

class IngredientAdapter(
    onClickListener: BaseClickListener<ExtendedIngredientDomain>,
) : BaseAdapter<ExtendedIngredientDomain, IngredientItemBinding>(
    onClickListener,
    { old, new -> old.exIngredientId == new.exIngredientId },
    { old, new -> old === new }
) {

    override val layoutRes: Int
        get() = R.layout.ingredient_item

    override fun bind(
        holder: BaseViewHolder<IngredientItemBinding>,
        clickListener: BaseClickListener<ExtendedIngredientDomain>,
        item: ExtendedIngredientDomain
    ) {
        with(dataBinding) {
            ingredientItem = item
            ingredientCallback = clickListener
        }
    }
}