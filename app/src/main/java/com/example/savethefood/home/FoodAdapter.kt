package com.example.savethefood.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.databinding.FoodItemBinding
import com.example.savethefood.data.domain.FoodDomain

class FoodAdapter(
    private val onClickListener: OnClickListener
) : ListAdapter<FoodDomain, FoodAdapter.FoodViewHolder>(
    DiffCallback
) {
    class FoodViewHolder private constructor(
        val binding: FoodItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: OnClickListener, item: FoodDomain) {
            with(binding) {
                foodDomain = item
                foodCallback = clickListener
                executePendingBindings()
            }
        }

        //With a companion object we can get a function or a property to be tied to a class rather than to instances of it
        companion object {
            val from = {parent: ViewGroup ->
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FoodItemBinding.inflate(layoutInflater, parent, false)
                FoodViewHolder(binding)
            }
            /*fun from(parent: ViewGroup): FoodViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FoodItemBinding.inflate(layoutInflater, parent, false)

                return FoodViewHolder(binding)
            }*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        return holder.bind(onClickListener, getItem(position))
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Food]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<FoodDomain>() {
        override fun areItemsTheSame(oldItem: FoodDomain, newItem: FoodDomain): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: FoodDomain, newItem: FoodDomain): Boolean {
            return oldItem.foodId == newItem.foodId
        }
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Food]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Food]
     */
    class OnClickListener(
        val clickListener: (food: FoodDomain) -> Unit
    ) {
        fun onClick(food: FoodDomain) = clickListener(food)
    }
}