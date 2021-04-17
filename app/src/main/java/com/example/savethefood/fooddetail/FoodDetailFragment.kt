package com.example.savethefood.fooddetail

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.savethefood.BaseFragment
import com.example.savethefood.Event
import com.example.savethefood.R
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.databinding.FragmentFoodDetailBinding
import com.example.savethefood.home.HomeFragmentContainerDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.pair_item.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

// TODO add cale dar with weekly meal plan, map with people who are sharing food (click on marker and see the list of foods)
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FoodDetailFragment : BaseFragment<FoodDetailViewModel, FragmentFoodDetailBinding>() {

    private val args: FoodDetailFragmentArgs by navArgs()

    override val viewModel by viewModels<FoodDetailViewModel>()

    override val layoutRes: Int
        get() = R.layout.fragment_food_detail

    override val classTag: String
        get() = FoodDetailFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun init() {
        with(dataBinding) {
            foodDetailViewModel = viewModel
            foodRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            foodRecyclerView.adapter =
                FoodPantryAdapter(
                    FoodPantryAdapter.PantryBaseClickListener(
                        clickListener = {},
                        clickViewListener = { food, view ->
                            val drawable = view.food_image_view.background as GradientDrawable
                            view.tag = view.tag != true
                            viewModel.updateRecipeList(food.title)
                            drawable.setStroke(8,
                                if (view.tag == true) {
                                    ContextCompat.getColor(requireContext(), R.color.customGreen)
                                } else {
                                    Color.WHITE
                                });
                        }
                    )
                )
            recipesRecycleView.layoutManager = LinearLayoutManager(activity)
            setHasOptionsMenu(true)
            recipesRecycleView.adapter =
                RecipeIngredientsAdapter(RecipeIngredientsAdapter.RecipeIngredientsClickListener(
                    clickListener = {},
                    clickSaveListener = { recipe, _ ->
                        viewModel.saveRecipe(recipe)
                    }
                ))
        }
        setHasOptionsMenu(true)
    }

    override fun activateObservers() {
        viewModel.food.observe(viewLifecycleOwner, {
        })

        viewModel.recipeAdded.observe(viewLifecycleOwner) {
            // TODO 
        }

        viewModel.editFoodEvent.observe(viewLifecycleOwner, ::navigateTo)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_edit, menu)
    }

    override fun <T> navigateTo(event: Event<T>?) {
        event?.let {
            if (it.hasBeenHandled) return
            when (val content = it.getContentIfNotHandled()) {
                is Boolean -> if (content) {
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is FoodDomain -> findNavController()
                    .navigate(FoodDetailFragmentDirections.actionFoodDetailFragmentToAddFoodFragment(
                        bundleOf(
                            "foodDomain" to content)))
                else -> Unit
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.edit) {
            viewModel.navigateToFoodEdit()
            return true
        } else if (item.itemId == R.id.delete) {
            AlertDialog.Builder(requireContext())
                .setTitle("Info")
                .setMessage("Do you want to delete the food?")
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                .setPositiveButton("Yes") { dialog, _ ->
                    viewModel.deleteFood()
                    dialog.dismiss()
                }
                .create()
                .show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityNavigator.applyPopAnimationsToPendingTransition(requireNotNull(activity))
    }
}