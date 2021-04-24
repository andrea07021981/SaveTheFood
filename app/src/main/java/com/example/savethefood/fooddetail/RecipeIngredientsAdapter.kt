package com.example.savethefood.fooddetail

import android.view.View
import com.example.savethefood.BaseAdapter
import com.example.savethefood.R
import com.example.savethefood.data.domain.RecipeIngredients
import com.example.savethefood.databinding.PairRecipeItemBinding

class RecipeIngredientsAdapter(
    onClickListener: BaseClickListener<RecipeIngredients>,
) : BaseAdapter<RecipeIngredients, PairRecipeItemBinding>(
    onClickListener,
    { old, new -> old.id == new.id },
    { old, new -> old == new }
) {

    override val layoutRes: Int
        get() = R.layout.pair_recipe_item

    override fun bind(
        holder: BaseViewHolder<PairRecipeItemBinding>,
        clickListener: BaseClickListener<RecipeIngredients>,
        item: RecipeIngredients
    ) {
        with(dataBinding) {
            recipe = item
            callback = clickListener as RecipeIngredientsClickListener?
        }
    }

    class RecipeIngredientsClickListener(
        override val clickListener: (food: RecipeIngredients) -> Unit,
        val clickSaveListener: (recipe: RecipeIngredients, view: View) -> Unit
    ) : BaseClickListener<RecipeIngredients>(clickListener) {

        fun onSaveRecipe(recipe: RecipeIngredients, view: View) = clickSaveListener(recipe, view)
    }
}