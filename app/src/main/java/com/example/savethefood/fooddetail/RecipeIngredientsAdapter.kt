package com.example.savethefood.fooddetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.data.domain.RecipeIngredients
import com.example.savethefood.databinding.PairRecipeItemBinding

// TODO create a generic adapter or an abstract class
class RecipeIngredientsAdapter(
    private val onClickListener: OnClickListener
) : ListAdapter<RecipeIngredients, RecipeIngredientsAdapter.RecipeIngredientsHolder>(
    DiffCallback
) {
    class RecipeIngredientsHolder private constructor(
        val binding: PairRecipeItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: OnClickListener, item: RecipeIngredients) {
            with(binding) {
                recipe = item
                callback = clickListener
                executePendingBindings()
            }
        }

        //With a companion object we can get a function or a property to be tied to a class rather than to instances of it
        companion object {
            val from = {parent: ViewGroup ->
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PairRecipeItemBinding.inflate(layoutInflater, parent, false)
                RecipeIngredientsHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeIngredientsHolder {
        return RecipeIngredientsHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: RecipeIngredientsHolder, position: Int) {
        return holder.bind(onClickListener, getItem(position))
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Food]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<RecipeIngredients>() {
        override fun areItemsTheSame(oldItem: RecipeIngredients, newItem: RecipeIngredients): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: RecipeIngredients, newItem: RecipeIngredients): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Food]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Food]
     */
    class OnClickListener(
        val clickListener: (recipe: RecipeIngredients) -> Unit
    ) {
        fun onClick(recipe: RecipeIngredients) = clickListener(recipe)
    }
}