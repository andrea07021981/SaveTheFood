package com.example.savethefood.cook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.databinding.FragmentRecipeCookBinding
import com.example.savethefood.data.domain.RecipeInfoDomain

class RecipeCookFragment : Fragment() {

    private val recipeCookViewModel by viewModels<RecipeCookViewModel> {
        val application = requireNotNull(activity).application
        RecipeCookViewModel.Factory(application = application, recipe = recipeInfoSelected)
    }

    private lateinit var recipeInfoSelected: RecipeInfoDomain
    private lateinit var dataBinding: FragmentRecipeCookBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        dataBinding = FragmentRecipeCookBinding.inflate(inflater)
        recipeInfoSelected = RecipeCookFragmentArgs.fromBundle(
            requireArguments()
        ).recipeInfoDomain
        dataBinding.lifecycleOwner = this
        dataBinding.recipeCookViewModel = recipeCookViewModel

        dataBinding.stepRecycleView.layoutManager = LinearLayoutManager(activity)
        dataBinding.stepRecycleView.adapter =
            StepCookAdapter(StepCookAdapter.OnStepClickListener {
            })
        return dataBinding.root
    }

}