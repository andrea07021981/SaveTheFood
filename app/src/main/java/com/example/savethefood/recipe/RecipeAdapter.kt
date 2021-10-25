package com.example.savethefood.recipe

import com.example.savethefood.BaseAdapter
import com.example.savethefood.R
import com.example.savethefood.databinding.RecipeItemBinding
import com.example.savethefood.shared.data.domain.RecipeResult

class RecipeAdapter(
    onClickListener: BaseClickListener<RecipeResult>,
) : BaseAdapter<RecipeResult, RecipeItemBinding>(
    onClickListener,
    { old, new -> old.id == new.id },
    { old, new -> old === new }
) {

    override val layoutRes: Int
        get() = R.layout.recipe_item

    override fun bind(
        holder: BaseViewHolder<RecipeItemBinding>,
        clickListener: BaseClickListener<RecipeResult>,
        item: RecipeResult
    ) {
        with(holder.binding) {
            recipeItem = item
            recipeCallback = clickListener
        }
    }
}