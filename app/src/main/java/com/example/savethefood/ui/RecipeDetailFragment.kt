package com.example.savethefood.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.savethefood.databinding.FragmentRecipeDetailBinding
import com.example.savethefood.viewmodel.RecipeDetailViewModel

class RecipeDetailFragment : Fragment() {

    private val recipeDetailViewModel: RecipeDetailViewModel by lazy {
        val application = requireNotNull(activity).application
        ViewModelProvider(requireNotNull(activity,
            {
                RecipeDetailViewModel.Factory(app = application, id = recipeId)
            })).get(RecipeDetailViewModel::class.java)
    }

    private var recipeId: Int = 0
    private lateinit var dataBinding: FragmentRecipeDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentRecipeDetailBinding.inflate(inflater)
        dataBinding.lifecycleOwner = this
        dataBinding.recipeDetailViewModel = recipeDetailViewModel
        return dataBinding.root
    }

}