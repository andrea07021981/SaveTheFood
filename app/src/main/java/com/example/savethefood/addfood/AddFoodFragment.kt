package com.example.savethefood.addfood

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.example.savethefood.BaseFragment
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentAddFoodBinding
import com.example.savethefood.ui.FoodItem
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
            duration = resources.getInteger(android.R.integer.config_longAnimTime).toLong()
        }
    }

    override fun init() {
        super.init()

        with(dataBinding) {
            addFoodViewModel = viewModel
        }
    }

    override fun onResume() {
        super.onResume()
        setFragmentResultListener("result") { _, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result = bundle.get("foodItem") as FoodItem
            viewModel.updateFood(result)
        }
    }
    override fun activateObservers() {
        viewModel.openFoodTypeDialog.observe(viewLifecycleOwner) {
            SearchableFragment().show(parentFragmentManager, classTag)
        }

        viewModel.foodItem.observe(viewLifecycleOwner) {
            Log.d(classTag, "Updated item")
        }
    }
}