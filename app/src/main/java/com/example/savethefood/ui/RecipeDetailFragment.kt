package com.example.savethefood.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.savethefood.databinding.FragmentRecipeDetailBinding
import com.example.savethefood.local.domain.RecipeDomain
import com.example.savethefood.local.domain.RecipeResult
import com.example.savethefood.viewmodel.RecipeDetailViewModel
import com.example.savethefood.viewmodel.RecipeViewModel

class RecipeDetailFragment : Fragment() {

    private val recipeDetailViewModel: RecipeDetailViewModel by lazy {
        val application = requireNotNull(activity).application
        ViewModelProvider(requireNotNull(activity), RecipeDetailViewModel.Factory(application = application, recipeResult = recipeSelected))
            .get(RecipeDetailViewModel::class.java)
    }

    private lateinit var recipeSelected: RecipeResult
    private lateinit var dataBinding: FragmentRecipeDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentRecipeDetailBinding.inflate(inflater)
        recipeSelected = RecipeResult(1, "", "", "", 3, 3, "")
        dataBinding.lifecycleOwner = this
        dataBinding.recipeDetailViewModel = recipeDetailViewModel
        return dataBinding.root
    }

}