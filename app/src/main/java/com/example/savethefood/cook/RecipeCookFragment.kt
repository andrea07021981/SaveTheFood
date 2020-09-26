package com.example.savethefood.cook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.databinding.FragmentRecipeCookBinding
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.home.HomeFragmentArgs
import com.example.savethefood.recipe.RecipeFragmentArgs

class RecipeCookFragment : Fragment() {

    private val args: RecipeCookFragmentArgs by navArgs()
    private val recipeCookViewModel by viewModels<RecipeCookViewModel> {
        RecipeCookViewModel.Factory(recipe = args.recipeInfoDomain)
    }

    private lateinit var dataBinding: FragmentRecipeCookBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        dataBinding = FragmentRecipeCookBinding.inflate(inflater).also {
            it.lifecycleOwner = this
            it.recipeCookViewModel = recipeCookViewModel
            it.stepRecycleView.layoutManager = LinearLayoutManager(activity)
            it.stepRecycleView.adapter =
                StepCookAdapter(StepCookAdapter.OnStepClickListener {
                })
        }

        return dataBinding.root
    }

}