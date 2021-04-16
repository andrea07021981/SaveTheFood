package com.example.savethefood.recipe

import com.example.savethefood.BaseAdapter
import com.example.savethefood.R
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.databinding.RecipeItemBinding

class RecipeAdapter(
    onClickListener: BaseClickListener<RecipeResult>,
) : BaseAdapter<RecipeResult, RecipeItemBinding>(onClickListener) {

    override val layoutRes: Int
        get() = R.layout.recipe_item

    override fun bind(
        holder: BaseViewHolder<RecipeItemBinding>,
        clickListener: BaseClickListener<RecipeResult>,
        item: RecipeResult
    ) {
        with(dataBinding) {
            recipeItem = item
            recipeCallback = clickListener
        }
    }
}