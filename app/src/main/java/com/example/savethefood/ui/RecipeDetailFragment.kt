package com.example.savethefood.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.component.IngredientAdapter
import com.example.savethefood.databinding.FragmentRecipeDetailBinding
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.viewmodel.RecipeDetailViewModel

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
    ) : View? {
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

        recipeDetailViewModel.navigateToRecipeCooking.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                findNavController()
                    .navigate(RecipeDetailFragmentDirections.actionRecipeDetailFragmentToRecipeCookFragment(it))
                recipeDetailViewModel.doneToCookDetail()
            }
        })
        return dataBinding.root
    }

}