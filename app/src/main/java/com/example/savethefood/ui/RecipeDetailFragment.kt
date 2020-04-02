package com.example.savethefood.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.savethefood.databinding.FragmentRecipeDetailBinding
import com.example.savethefood.local.domain.RecipeResult
import com.example.savethefood.viewmodel.RecipeDetailViewModel

class RecipeDetailFragment : Fragment() {

    private val recipeDetailViewModel: RecipeDetailViewModel by lazy {
        val application = requireNotNull(activity).application
        ViewModelProvider(requireNotNull(activity,
            {
                RecipeDetailViewModel.Factory(app = application, recipe = recipeSelected)
            })).get(RecipeDetailViewModel::class.java)
    }

    private lateinit var recipeSelected: RecipeResult
    private lateinit var dataBinding: FragmentRecipeDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentRecipeDetailBinding.inflate(inflater)
        recipeSelected = RecipeDetailFragmentArgs.fromBundle(requireArguments()).recipeResult
        dataBinding.lifecycleOwner = this
        dataBinding.recipeDetailViewModel = recipeDetailViewModel
        return dataBinding.root
    }

}