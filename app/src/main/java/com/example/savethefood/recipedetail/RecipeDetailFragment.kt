package com.example.savethefood.recipedetail

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.BaseAdapter
import com.example.savethefood.BaseFragment
import com.example.savethefood.EventObserver
import com.example.savethefood.R
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.repository.RecipeDataRepository
import com.example.savethefood.databinding.FragmentRecipeDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailFragment : BaseFragment<RecipeDetailViewModel, FragmentRecipeDetailBinding>() {

    private lateinit var recipeSelected: RecipeResult

    override val viewModel by viewModels<RecipeDetailViewModel>()

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
        with(dataBinding) {
            recipeDetailViewModel = viewModel
            ingredientRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            ingredientRecyclerView.adapter =
                IngredientAdapter(
                    BaseAdapter.BaseClickListener {
                        //TODO OPEN ALER DIALOG WITH CUSTOM LAYOUT INGREDIENT DETAIL
                        Log.d(classTag, it.exIngredientName)
                    })
            maintoolbar.setNavigationOnClickListener {
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
        viewModel.recipeCookingEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                if (it.recipeAnalyzedInstructions.isNullOrEmpty()) {
                    Toast.makeText(
                        context,
                        "Instructions are not available for this recipe",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    findNavController()
                        .navigate(
                            RecipeDetailFragmentDirections.actionRecipeDetailFragmentToRecipeCookFragment(
                                it
                            )
                        )
                }
            }
        })
    }
}