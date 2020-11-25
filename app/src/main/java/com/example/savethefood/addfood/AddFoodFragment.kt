package com.example.savethefood.addfood

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import com.example.savethefood.BaseFragment
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentAddFoodBinding
import com.example.savethefood.ui.FoodItem
import com.example.savethefood.ui.FoodSpinnerAdapter
import com.example.savethefood.util.FoodImage
import com.google.android.material.transition.MaterialFadeThrough

class AddFoodFragment : BaseFragment<AddFoodViewModel, FragmentAddFoodBinding>() {

    override val viewModel by viewModels<AddFoodViewModel>()

    override val layoutRes: Int
        get() = R.layout.fragment_add_food

    override val classTag: String
        get() = AddFoodFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            addTarget(R.id.food_item_root)
            duration = 1500
        }
    }

    override fun init() {
        super.init()

        dataBinding.also {
            it.addFoodViewModel = viewModel
            val customObjects = viewModel.foodItems
            val adapter = FoodSpinnerAdapter(
                requireNotNull(activity),
                customObjects
            )

            it.spinner.adapter = adapter
            it.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.d("CLick", id.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.d("CLick", "nothing")
                }

            }
        }
    }

    override fun activateObservers() {

    }
}