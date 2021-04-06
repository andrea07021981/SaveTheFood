package com.example.savethefood.fooddetail

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.savethefood.BaseFragment
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentFoodDetailBinding
import com.example.savethefood.home.FoodAdapter
import com.example.savethefood.recipe.RecipeAdapter
import com.example.savethefood.recipedetail.IngredientAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.food_item.view.*
import kotlinx.android.synthetic.main.fragment_food.*
import kotlinx.android.synthetic.main.pair_item.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


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
                    FoodPantryAdapter.OnClickListener { food, view ->
                        val drawable = view.food_image_view.background as GradientDrawable
                        view.tag = view.tag != true
                        viewModel.updateRecipeList(food.title)
                        drawable.setStroke(8,
                            if (view.tag == true) {
                                ContextCompat.getColor(requireContext(), R.color.customGreen)
                            } else {
                                Color.WHITE
                            });
                    })
            recipesRecycleView.layoutManager = LinearLayoutManager(activity)
            setHasOptionsMenu(true)
            recipesRecycleView.adapter =
                RecipeIngredientsAdapter(RecipeIngredientsAdapter.OnClickListener(
                    clickListener = {
                        // TODO move to recipe cook
                    },
                    clickSaveListener = {
                        viewModel.saveRecipe(it)
                    }))
        }
    }

    override fun activateObservers() {
        viewModel.food.observe(viewLifecycleOwner, {
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityNavigator.applyPopAnimationsToPendingTransition(requireNotNull(activity))
    }
}