package com.example.savethefood.cook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.databinding.IngredientInstructionItemBinding
import com.example.savethefood.data.domain.IngredientsDomain

class IngredientInstructionAdapter : ListAdapter<IngredientsDomain, IngredientInstructionAdapter.IngredientInstructionViewHolder>(
    DiffCallback
) {

    class IngredientInstructionViewHolder private constructor(
        val binding: IngredientInstructionItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: IngredientsDomain) {
            with(binding) {
                ingredientItem = item
                executePendingBindings()
            }
        }

        companion object {
            val from = {parent: ViewGroup ->
                val layoutInflate = LayoutInflater.from(parent.context)
                val binding = IngredientInstructionItemBinding.inflate(layoutInflate, parent, false)
                IngredientInstructionViewHolder(
                    binding
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientInstructionViewHolder {
        return IngredientInstructionViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: IngredientInstructionViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Food]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<IngredientsDomain>() {
        override fun areItemsTheSame(oldItem: IngredientsDomain, newItem: IngredientsDomain): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: IngredientsDomain, newItem: IngredientsDomain): Boolean {
            return oldItem.ingredientId == newItem.ingredientId
        }
    }

}