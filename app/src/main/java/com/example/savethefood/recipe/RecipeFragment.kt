package com.example.savethefood.recipe

import android.content.Context
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.BaseAdapter
import com.example.savethefood.BaseFragment
import com.example.savethefood.EventObserver
import com.example.savethefood.R
import com.example.savethefood.constants.Constants
import com.example.savethefood.constants.RecipeType
import com.example.savethefood.constants.StorageType
import com.example.savethefood.cook.RecipeCookFragmentArgs
import com.example.savethefood.data.source.repository.RecipeDataRepository
import com.example.savethefood.databinding.FragmentReceipeBinding
import com.example.savethefood.util.configSearchView
import dagger.hilt.android.AndroidEntryPoint

// TODO use paging library
@AndroidEntryPoint
class RecipeFragment : BaseFragment<RecipeViewModel, FragmentReceipeBinding>() {

    private var foodName: String? = null

    override val viewModel: RecipeViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override val layoutRes: Int
        get() = R.layout.fragment_receipe

    override val classTag: String
        get() = RecipeFragment::class.java.simpleName

    override fun init() {
        // TODO review it
        /*if (arguments?.isEmpty == false) {
            foodName = RecipeFragmentArgs.fromBundle(
                requireArguments()
            ).foodName
        }*/
        with(dataBinding) {
            recipeViewModel = viewModel
            recipeRecycleview.layoutManager = LinearLayoutManager(activity)
            setHasOptionsMenu(true)
            recipeRecycleview.adapter =
                RecipeAdapter(BaseAdapter.BaseClickListener { recipeResult ->
                    viewModel.moveToRecipeDetail(recipeResult)
                })
            recipeRecycleview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    viewModel.reloadList()
                }
            })
            arguments?.takeIf { it.containsKey(Constants.RECIPE_LIST) }?.apply {
                val recipeType = getSerializable(Constants.RECIPE_LIST) as RecipeType
                Log.d(classTag, recipeType.type)
            }
        }
    }

    override fun activateObservers() {
        viewModel.recipeDetailEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                val bundle = bundleOf("recipeResult" to it)
                findNavController()
                    .navigate(R.id.recipeDetailFragment, bundle, null, null)
            }
        })

        viewModel.recipeListResult.observe(this.viewLifecycleOwner, {
            (dataBinding.recipeRecycleview.adapter as RecipeAdapter).notifyDataSetChanged()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchItem.configSearchView(
            requireNotNull(activity),
        "Search Recipes"
        ) {
            dataBinding.recipeViewModel?.updateDataList(it)
        }
    }

    private fun hideKeyboard() {
        val inputManager:InputMethodManager = requireNotNull(activity)
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(requireNotNull(activity).currentFocus?.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
    }

    override fun onDestroy() {
        super.onDestroy()
        hideKeyboard()
    }
}