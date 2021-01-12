package com.example.savethefood.addfood

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.transition.Visibility
import com.example.savethefood.BaseFragment
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentAddFoodBinding
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
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
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