package com.example.savethefood.component

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.databinding.StepCookItemBinding
import com.example.savethefood.local.domain.StepDomain

class StepCookAdapter(
    private val onClickListener: OnStepClickListener
) : ListAdapter<StepDomain, StepCookAdapter.StepCookViewHolder>(DiffCallback) {

    class StepCookViewHolder private constructor(
        val binding: StepCookItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OnStepClickListener, item: StepDomain) {
            with(binding) {
                stepItem = item
                stepCallback = clickListener
                executePendingBindings()
            }
        }

        companion object {
            val from = { parent: ViewGroup ->
                val layoutInflate = LayoutInflater.from(parent.context)
                val binding = StepCookItemBinding.inflate(layoutInflate, parent, false)
                StepCookViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepCookViewHolder {
        return StepCookViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: StepCookViewHolder, position: Int) {
        return holder.bind(onClickListener, getItem(position))
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Food]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<StepDomain>() {
        override fun areItemsTheSame(oldItem: StepDomain, newItem: StepDomain): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: StepDomain, newItem: StepDomain): Boolean {
            return oldItem.stepNumber== newItem.stepNumber
        }
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Step]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Step]
     */
    class OnStepClickListener(
        val clickListener: (step: StepDomain) -> Unit
    ) {
        fun onClick(step: StepDomain) = clickListener(step)
    }
}
