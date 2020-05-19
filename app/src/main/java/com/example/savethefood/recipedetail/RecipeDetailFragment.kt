package com.example.savethefood.recipedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.EventObserver
import com.example.savethefood.databinding.FragmentRecipeDetailBinding
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.repository.RecipeDataRepository

class RecipeDetailFragment : Fragment() {

    private val recipeDetailViewModel by viewModels<RecipeDetailViewModel> {
        RecipeDetailViewModel.RecipeDetailViewModelFactory(RecipeDataRepository.getRepository(requireActivity().application), recipeSelected)
    }

    private lateinit var recipeSelected: RecipeResult
    private lateinit var dataBinding: FragmentRecipeDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        dataBinding = FragmentRecipeDetailBinding.inflate(inflater)
        recipeSelected = RecipeDetailFragmentArgs.fromBundle(
            requireArguments()
        ).recipeResult
        dataBinding.lifecycleOwner = this
        dataBinding.recipeDetailViewModel = recipeDetailViewModel

        dataBinding.ingredientRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        dataBinding.ingredientRecyclerView.adapter =
            IngredientAdapter(
                IngredientAdapter.OnIngredientClickListener {
                    //TODO OPEN ALER DIALOG WITH CUSTOM LAYOUT INGREDIENT DETAIL
                })
        dataBinding.maintoolbar.setNavigationOnClickListener {
            recipeDetailViewModel.backToRecipeList()
        }

        recipeDetailViewModel.recipeListEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                findNavController()
                    .popBackStack()
            }
        })

        //TODO add save recipe into DB
        
        recipeDetailViewModel.recipeCookingtEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                findNavController()
                    .navigate(
                        RecipeDetailFragmentDirections.actionRecipeDetailFragmentToRecipeCookFragment(
                            it
                        )
                    )
            }
        })
        return dataBinding.root
    }

}