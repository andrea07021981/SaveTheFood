package com.example.savethefood.recipedetail

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.BaseFragment
import com.example.savethefood.EventObserver
import com.example.savethefood.R
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.repository.RecipeDataRepository
import com.example.savethefood.databinding.FragmentRecipeDetailBinding

class RecipeDetailFragment : BaseFragment<RecipeDetailViewModel, FragmentRecipeDetailBinding>() {

    private lateinit var recipeSelected: RecipeResult

    override val viewModel by viewModels<RecipeDetailViewModel> {
        RecipeDetailViewModel.RecipeDetailViewModelFactory(RecipeDataRepository.getRepository(requireActivity().application), recipeSelected)
    }

    override val layoutRes: Int
        get() = R.layout.fragment_recipe_detail

    override val classTag: String
        get() = RecipeDetailFragment::class.java.simpleName

    override fun init() {
        if (arguments?.isEmpty == false) {
            recipeSelected = RecipeDetailFragmentArgs.fromBundle(
                requireArguments()
            ).recipeResult
        }
        dataBinding.also {
            it.recipeDetailViewModel = viewModel

            it.ingredientRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            it.ingredientRecyclerView.adapter =
                IngredientAdapter(
                    IngredientAdapter.OnIngredientClickListener {
                        //TODO OPEN ALER DIALOG WITH CUSTOM LAYOUT INGREDIENT DETAIL
                    })
            it.maintoolbar.setNavigationOnClickListener {
                viewModel.backToRecipeList()
            }
        }
    }

    override fun activateObservers() {
        viewModel.recipeListEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                findNavController()
                    .popBackStack()
            }
        })

        //TODO add save recipe into DB
        viewModel.recipeCookingtEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                findNavController()
                    .navigate(
                        RecipeDetailFragmentDirections.actionRecipeDetailFragmentToRecipeCookFragment(
                            it
                        )
                    )
            }
        })
    }
}