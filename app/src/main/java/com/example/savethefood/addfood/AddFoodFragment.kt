package com.example.savethefood.addfood

import androidx.fragment.app.viewModels
import com.example.savethefood.BaseFragment
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentAddFoodBinding

class AddFoodFragment : BaseFragment<AddFoodViewModel, FragmentAddFoodBinding>() {

    override val viewModel by viewModels<AddFoodViewModel>()

    override val layoutRes: Int
        get() = R.layout.fragment_add_food

    override val classTag: String
        get() = AddFoodFragment::class.java.simpleName

    override fun init() {
        super.init()
        dataBinding.also {
            it.addFoodViewModel = viewModel
        }
    }

    override fun activateObservers() {

    }
}