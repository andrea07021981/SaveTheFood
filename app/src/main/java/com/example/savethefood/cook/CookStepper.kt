package com.example.savethefood.cook

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.R
import com.example.savethefood.data.domain.StepDomain
import com.example.savethefood.databinding.StepCookItemBinding
import ernestoyaquello.com.verticalstepperform.Step

class CookStepper(
    title: String?,
    val data: StepDomain?
) : Step<StepDomain>(title) {
    private val layoutRes = R.layout.step_cook_item
    lateinit var dataBinding: StepCookItemBinding
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun getStepData(): StepDomain {
        return data!!
    }

    override fun getStepDataAsHumanReadableString(): String {
        return title
    }

    override fun restoreStepData(data: StepDomain?) {

    }

    override fun isStepDataValid(stepData: StepDomain?): IsDataValid {
        return IsDataValid(true, "no message")
    }

    override fun createStepContentLayout(): View {
        dataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            layoutRes,
            this.formView, false)

        with(dataBinding) {
            stepItem = data
            val ingredientManager = LinearLayoutManager(ingredientsRecyclerView.context, LinearLayoutManager.HORIZONTAL, false)
            ingredientsRecyclerView.apply {
                layoutManager = ingredientManager
                adapter =
                    IngredientInstructionAdapter()
                setRecycledViewPool(viewPool)
            }
            val equipmentManager = LinearLayoutManager(equipmentsRecyclerView.context, LinearLayoutManager.HORIZONTAL, false)
            equipmentsRecyclerView.apply {
                layoutManager = equipmentManager
                adapter =
                    EquipmentInstructionAdapter()
                setRecycledViewPool(viewPool)
            }
        }
        return dataBinding.root;
    }

    override fun onStepOpened(animated: Boolean) {
    }

    override fun onStepClosed(animated: Boolean) {

    }

    override fun onStepMarkedAsCompleted(animated: Boolean) {

    }

    override fun onStepMarkedAsUncompleted(animated: Boolean) {

    }
}