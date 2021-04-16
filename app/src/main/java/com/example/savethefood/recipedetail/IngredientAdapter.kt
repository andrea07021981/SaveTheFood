package com.example.savethefood.recipedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.BaseAdapter
import com.example.savethefood.R
import com.example.savethefood.databinding.IngredientItemBinding
import com.example.savethefood.data.domain.ExtendedIngredientDomain

class IngredientAdapter(
    onClickListener: BaseClickListener<ExtendedIngredientDomain>,
) : BaseAdapter<ExtendedIngredientDomain, IngredientItemBinding>(onClickListener) {

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