package com.example.savethefood.component

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.databinding.RecipeItemBinding
import com.example.savethefood.data.domain.RecipeResult

class RecipeAdapter(
    private val onClickListener: OnClickListener
) : ListAdapter<RecipeResult, RecipeAdapter.RecipeViewHolder>(DiffCallback){

    class RecipeViewHolder private constructor(
        val binding: RecipeItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OnClickListener, item: RecipeResult) {
            with(binding) {
                recipeItem = item
                recipeCallback = clickListener
                executePendingBindings()
            }
        }

        companion object {
            val from = {parent: ViewGroup ->
                val layoutInflate = LayoutInflater.from(parent.context)
                val binding = RecipeItemBinding.inflate(layoutInflate, parent, false)
                RecipeViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        return holder.bind(onClickListener, getItem(position))
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Food]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<RecipeResult>() {
        override fun areItemsTheSame(oldItem: RecipeResult, newItem: RecipeResult): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: RecipeResult, newItem: RecipeResult): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Food]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Food]
     */
    class OnClickListener(
        val clickListener: (recipe: RecipeResult) -> Unit
    ) {
        fun onClick(recipe: RecipeResult) = clickListener(recipe)
    }
}