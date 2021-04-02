package com.example.savethefood.fooddetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.databinding.PairItemBinding

// TODO create a generic adapter or an abstract class
class FoodPantryAdapter(
    private val onClickListener: OnClickListener
) : ListAdapter<FoodDomain, FoodPantryAdapter.FoodViewHolder>(
    DiffCallback
) {
    class FoodViewHolder private constructor(
        val binding: PairItemBinding
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
                val binding = PairItemBinding.inflate(layoutInflater, parent, false)
                FoodViewHolder(binding)
            }
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
            return oldItem.id == newItem.id
        }
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Food]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Food]
     */
    class OnClickListener(
        val clickListener: (food: FoodDomain, view: View) -> Unit
    ) {
        fun onClick(food: FoodDomain, view: View) = clickListener(food, view)
    }
}