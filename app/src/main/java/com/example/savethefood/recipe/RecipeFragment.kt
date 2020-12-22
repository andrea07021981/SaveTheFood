package com.example.savethefood.recipe

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.EventObserver
import com.example.savethefood.R
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.repository.RecipeDataRepository
import com.example.savethefood.databinding.FragmentReceipeBinding
import com.example.savethefood.util.configSearchView

// TODO add tablayout, recipe online and recipe saved (need room data entities)
class RecipeFragment : Fragment() {

    private var foodName: String? = null
    private val recipeViewModel by viewModels<RecipeViewModel> {
        RecipeViewModel.RecipeViewModelFactory(RecipeDataRepository.getRepository(requireActivity().application), foodName)
    }

    private lateinit var dataBinding: FragmentReceipeBinding
    private var searchView: SearchView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        if (arguments?.isEmpty == false) foodName = RecipeFragmentArgs.fromBundle(
            requireArguments()
        ).foodName
        dataBinding  = FragmentReceipeBinding.inflate(inflater).also {
            it.lifecycleOwner = this
            it.recipeViewModel = recipeViewModel
            it.recipeRecycleview.layoutManager = LinearLayoutManager(activity)
            setHasOptionsMenu(true)
            it.recipeRecycleview.adapter =
                RecipeAdapter(RecipeAdapter.OnClickListener { recipeResult ->
                    recipeViewModel.moveToRecipeDetail(recipeResult)
                })
            it.recipeRecycleview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    recipeViewModel.reloadList()
                }
            })
        }

        activateObservers()
        return dataBinding.root
    }

    private fun activateObservers() {
        recipeViewModel.recipeDetailEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                val bundle = bundleOf("recipeResult" to it)
                findNavController()
                    .navigate(R.id.recipeDetailFragment, bundle, null, null)
            }
        })

        recipeViewModel.recipeListResult.observe(this.viewLifecycleOwner, {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
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