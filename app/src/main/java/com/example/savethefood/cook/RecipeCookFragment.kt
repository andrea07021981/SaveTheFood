package com.example.savethefood.cook

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.BaseFragment
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentRecipeCookBinding

class RecipeCookFragment : BaseFragment<RecipeCookViewModel, FragmentRecipeCookBinding>() {

    private val args: RecipeCookFragmentArgs by navArgs()

    override val viewModel by viewModels<RecipeCookViewModel> {
        RecipeCookViewModel.Factory(recipe = args.recipeInfoDomain)
    }

    override val layoutRes: Int
        get() = R.layout.fragment_recipe_cook

    override val classTag: String
        get() = RecipeCookFragment::class.java.simpleName

    override fun init() {
        with(dataBinding) {
            recipeCookViewModel = viewModel
            stepRecycleView.layoutManager = LinearLayoutManager(activity)
            stepRecycleView.adapter =
                StepCookAdapter(StepCookAdapter.OnStepClickListener {
                })
        }
    }
    override fun activateObservers() {

    }

}