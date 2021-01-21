package com.example.savethefood.addfood

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.databinding.FoodTypeItemBinding
import com.example.savethefood.data.domain.FoodItem

class FoodTypeAdapter(
    private val onClickListener: OnClickListener
) : ListAdapter<FoodItem, FoodTypeAdapter.FoodViewHolder>(
    DiffCallback
) {

    class FoodViewHolder private constructor(
        val binding: FoodTypeItemBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: OnClickListener, item: FoodItem) {
            with(binding) {
                foodItem = item
                foodCallback = clickListener
            }
        }

        companion object {
            val from = { parent: ViewGroup ->
                val inflate = LayoutInflater.from(parent.context)
                val binding = FoodTypeItemBinding.inflate(inflate, parent, false)
                FoodViewHolder(
                    binding
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(
            onClickListener,
            getItem(position)
        )
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Food]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<FoodItem>() {
        override fun areItemsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
            return oldItem.name == newItem.name
        }
    }

    class OnClickListener(
        val onClickListener: (FoodItem) -> Unit
    ) {
        fun onClick(item: FoodItem) = onClickListener(item)
    }
}