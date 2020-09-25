package com.example.savethefood.food

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.data.domain.FoodSearchDomain
import com.example.savethefood.data.domain.ProductDomain
import com.example.savethefood.databinding.FoodSearchItemBinding

class FoodSearchAdapter(
    private val onClickListener: OnClickListener
) : ListAdapter<ProductDomain, FoodSearchAdapter.FoodSearchViewHolder>(
    DiffCallback
) {

    class FoodSearchViewHolder private constructor(
        val binding: FoodSearchItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OnClickListener, item: ProductDomain) {
            with(binding) {
                productItem = item
                searchFoodCallback = clickListener
                executePendingBindings()
            }
        }

        companion object {
            val from = { parent: ViewGroup ->
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FoodSearchItemBinding.inflate(layoutInflater, parent, false)
                FoodSearchViewHolder(
                    binding
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodSearchViewHolder {
        return FoodSearchViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: FoodSearchViewHolder, position: Int) {
        return holder.bind(
            onClickListener,
            getItem(position)
        )
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [FoodSearchDomain]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<ProductDomain>() {
        override fun areItemsTheSame(oldItem: ProductDomain, newItem: ProductDomain): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ProductDomain, newItem: ProductDomain): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * Class listener for click
     */
    class OnClickListener(
        val clickListener: (searchFood: ProductDomain) -> Unit
    ) {
        fun onClick(food: ProductDomain) = clickListener(food)
    }
}