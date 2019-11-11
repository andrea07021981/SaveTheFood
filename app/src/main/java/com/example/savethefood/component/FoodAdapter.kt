package com.example.savethefood.component

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.databinding.FoodItemBinding
import com.example.savethefood.local.domain.Food

class FoodAdapter(
    val onClickListener: OnClickListener
) : ListAdapter<Food, FoodAdapter.FoodViewHolder>(DiffCallback) {

    class FoodViewHolder private constructor(val binding: FoodItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OnClickListener, item: Food) {
            binding.food = item
            binding.foodCallback = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FoodViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FoodItemBinding.inflate(layoutInflater, parent, false)

                return FoodViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        return holder.bind(onClickListener, getItem(position))
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Food]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem.foodId == newItem.foodId
        }
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Food]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Food]
     */
    class OnClickListener(val clickListener: (food: Food) -> Unit) {
        fun onClick(food: Food) = clickListener(food)
    }
}