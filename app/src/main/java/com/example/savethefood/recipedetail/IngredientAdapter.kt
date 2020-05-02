package com.example.savethefood.recipedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.databinding.IngredientItemBinding
import com.example.savethefood.data.domain.ExtendedIngredientDomain

class IngredientAdapter(
    private val onClickListener: OnIngredientClickListener
) : ListAdapter<ExtendedIngredientDomain, IngredientAdapter.IngredientViewHolder>(
    DiffCallback
) {

    class IngredientViewHolder private constructor(
        val binding: IngredientItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OnIngredientClickListener, item: ExtendedIngredientDomain) {
            with(binding) {
                ingredientItem = item
                ingredientCallback = clickListener
                executePendingBindings()
            }
        }

        companion object {
            val from = {parent: ViewGroup ->
                val layoutInflate = LayoutInflater.from(parent.context)
                val binding = IngredientItemBinding.inflate(layoutInflate, parent, false)
                IngredientViewHolder(
                    binding
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        return holder.bind(onClickListener, getItem(position))
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Food]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<ExtendedIngredientDomain>() {
        override fun areItemsTheSame(oldItem: ExtendedIngredientDomain, newItem: ExtendedIngredientDomain): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ExtendedIngredientDomain, newItem: ExtendedIngredientDomain): Boolean {
            return oldItem.exIngredientId == newItem.exIngredientId
        }
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Food]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Food]
     */
    class OnIngredientClickListener(
        val clickListener: (ingredient: ExtendedIngredientDomain) -> Unit
    ) {
        fun onClick(ingredient: ExtendedIngredientDomain) = clickListener(ingredient)
    }
}