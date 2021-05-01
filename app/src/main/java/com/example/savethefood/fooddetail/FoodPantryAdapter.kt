package com.example.savethefood.fooddetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.BaseAdapter
import com.example.savethefood.R
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.RecipeIngredients
import com.example.savethefood.databinding.FoodItemBinding
import com.example.savethefood.databinding.PairItemBinding

class FoodPantryAdapter(
    onClickListener: BaseClickListener<FoodDomain>,
) : BaseAdapter<FoodDomain, PairItemBinding>(
    onClickListener,
    { old, new -> old === new },
    { old, new -> old.id == new.id }
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