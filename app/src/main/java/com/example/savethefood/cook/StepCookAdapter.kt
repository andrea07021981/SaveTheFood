package com.example.savethefood.cook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.databinding.StepCookItemBinding
import com.example.savethefood.data.domain.StepDomain

class StepCookAdapter(
    private val onClickListener: OnStepClickListener
) : ListAdapter<StepDomain, StepCookAdapter.StepCookViewHolder>(
    DiffCallback
) {

    class StepCookViewHolder private constructor(
        val binding: StepCookItemBinding,
        val parent: ViewGroup
    ) : RecyclerView.ViewHolder(binding.root) {

        private val viewPool = RecyclerView.RecycledViewPool()

        fun bind(clickListener: OnStepClickListener, item: StepDomain) {
            with(binding) {
                stepItem = item
                stepCallback = clickListener
                val ingredientManager = LinearLayoutManager(this.ingredientsRecyclerView.context, LinearLayoutManager.HORIZONTAL, false)
                this.ingredientsRecyclerView.apply {
                    layoutManager = ingredientManager
                    adapter =
                        IngredientInstructionAdapter()
                    setRecycledViewPool(viewPool)
                }
                val equipmentManager = LinearLayoutManager(this.equipmentsRecyclerView.context, LinearLayoutManager.HORIZONTAL, false)
                this.equipmentsRecyclerView.apply {
                    layoutManager = equipmentManager
                    adapter =
                        EquipmentInstructionAdapter()
                    setRecycledViewPool(viewPool)
                }
                executePendingBindings()
            }
        }

        companion object {
            val from = { parent: ViewGroup ->
                val layoutInflate = LayoutInflater.from(parent.context)
                val binding = StepCookItemBinding.inflate(layoutInflate, parent, false)
                StepCookViewHolder(
                    binding,
                    parent
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepCookViewHolder {
        return StepCookViewHolder.from(
            parent
        )
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
