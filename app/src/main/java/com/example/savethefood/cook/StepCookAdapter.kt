package com.example.savethefood.cook

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.BaseAdapter
import com.example.savethefood.R
import com.example.savethefood.databinding.StepCookItemBinding
import com.example.savethefood.shared.data.domain.StepDomain

class StepCookAdapter(
    onClickListener: BaseClickListener<StepDomain>,
) : BaseAdapter<StepDomain, StepCookItemBinding>(
    onClickListener,
    { old, new -> old.stepNumber == new.stepNumber },
    { old, new -> old == new }
) {

    override val layoutRes: Int
        get() = R.layout.step_cook_item

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun bind(
        holder: BaseViewHolder<StepCookItemBinding>,
        clickListener: BaseClickListener<StepDomain>,
        item: StepDomain
    ) {
        with(dataBinding) {
            stepItem = item
            stepCallback = clickListener
            val ingredientManager = LinearLayoutManager(this.ingredientsRecyclerView.context, LinearLayoutManager.HORIZONTAL, false)
            this.ingredientsRecyclerView.apply {
                layoutManager = ingredientManager
                adapter =
                    IngredientInstructionAdapter(BaseClickListener {  })
                setRecycledViewPool(viewPool)
            }
            val equipmentManager = LinearLayoutManager(this.equipmentsRecyclerView.context, LinearLayoutManager.HORIZONTAL, false)
            this.equipmentsRecyclerView.apply {
                layoutManager = equipmentManager
                adapter =
                    EquipmentInstructionAdapter(BaseClickListener {  })
                setRecycledViewPool(viewPool)
            }
            executePendingBindings()
        }
    }
}