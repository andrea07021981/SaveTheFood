package com.example.savethefood.recipedetail

import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.BaseAdapter
import com.example.savethefood.BaseFragment
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentRecipeDetailBinding
import com.example.savethefood.shared.data.domain.RecipeResult
import com.example.savethefood.shared.utils.EventObserver
import com.example.savethefood.shared.viewmodel.FoodDetailViewModel
import com.example.savethefood.shared.viewmodel.RecipeDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.viewmodel.ext.android.stateViewModel

@AndroidEntryPoint
class RecipeDetailFragment : BaseFragment<RecipeDetailViewModel, FragmentRecipeDetailBinding>() {

    private lateinit var recipeSelected: RecipeResult
    private val args: RecipeDetailFragmentArgs by navArgs()

    // TODO make this method of passing data standard (see fooddetailfragment), decide which one is better
    override val viewModel: RecipeDetailViewModel by stateViewModel(
        state = { bundleOf("recipeResult" to args.recipeResult )},
        clazz = RecipeDetailViewModel::class
    )

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
                        Log.d(classTag, it.exIngredientName ?: "No Message")
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