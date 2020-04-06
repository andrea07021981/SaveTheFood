package com.example.savethefood.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.component.IngredientAdapter
import com.example.savethefood.databinding.FragmentRecipeDetailBinding
import com.example.savethefood.local.domain.RecipeDomain
import com.example.savethefood.local.domain.RecipeResult
import com.example.savethefood.viewmodel.FoodDetailViewModel
import com.example.savethefood.viewmodel.RecipeDetailViewModel
import com.example.savethefood.viewmodel.RecipeViewModel
import kotlinx.android.synthetic.main.fragment_nested.*

class RecipeDetailFragment : Fragment() {

    private val recipeDetailViewModel: RecipeDetailViewModel by lazy {
        val application = requireNotNull(activity).application
        ViewModelProvider(this, RecipeDetailViewModel.Factory(application = application, recipeResult = recipeSelected))
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
        recipeSelected = RecipeDetailFragmentArgs.fromBundle(requireArguments()).recipeResult
        dataBinding.lifecycleOwner = this
        dataBinding.recipeDetailViewModel = recipeDetailViewModel

        dataBinding.ingredientRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        dataBinding.ingredientRecyclerView.adapter = IngredientAdapter(IngredientAdapter.OnIngredientClickListener {
            //TODO OPEN ALER DIALOG WITH CUSTOM LAYOUT INGREDIENT DETAIL
        })
        dataBinding.maintoolbar.setNavigationOnClickListener {
            recipeDetailViewModel.backToRecipeList()
        }

        recipeDetailViewModel.navigateToRecipeList.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                findNavController()
                    .popBackStack()
                recipeDetailViewModel.doneBackToRecipeList()
            }
        })
        return dataBinding.root
    }

}