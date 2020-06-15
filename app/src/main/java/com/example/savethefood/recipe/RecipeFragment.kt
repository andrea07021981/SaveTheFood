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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.EventObserver
import com.example.savethefood.R
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.repository.RecipeDataRepository
import com.example.savethefood.databinding.FragmentReceipeBinding


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
        dataBinding  = FragmentReceipeBinding.inflate(inflater)
        dataBinding.lifecycleOwner = this
        dataBinding.recipeViewModel = recipeViewModel
        //TODO manage screen landscape
        dataBinding.recipeRecycleview.layoutManager = LinearLayoutManager(activity)
        setHasOptionsMenu(true)
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.recipeRecycleview.adapter =
            RecipeAdapter(RecipeAdapter.OnClickListener {
                recipeViewModel.moveToRecipeDetail(it)
            })

        recipeViewModel.recipeDetailEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                val bundle = bundleOf("recipeResult" to it)
                findNavController()
                    .navigate(R.id.recipeDetailFragment, bundle, null, null)
            }
        })

        recipeViewModel.recipeListResult.observe(this.viewLifecycleOwner, Observer {
            (dataBinding.recipeRecycleview.adapter as RecipeAdapter).notifyDataSetChanged()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar, menu)
        val searchItem = menu.findItem(R.id.action_search)

        val searchManager =
            activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

        //Manage search view
        //making the searchview consume all the toolbar when open
        searchView?.let { it ->
            it.maxWidth= Int.MAX_VALUE
            it.queryHint = "Search Recipes"
            searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextChange(newText: String): Boolean {
                    //action while typing
                    dataBinding.recipeViewModel?.updateDataList(newText)
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    //action when type Enter
                    return false
                }

            })
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