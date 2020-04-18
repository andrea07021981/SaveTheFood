package com.example.savethefood.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadLayoutAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.R
import com.example.savethefood.component.RecipeAdapter
import com.example.savethefood.databinding.FragmentReceipeBinding
import com.example.savethefood.local.domain.FoodDomain
import com.example.savethefood.local.domain.RecipeDomain
import com.example.savethefood.local.domain.RecipeResult
import com.example.savethefood.viewmodel.RecipeViewModel
import kotlinx.android.synthetic.main.fragment_nested.*

class RecipeFragment : Fragment() {

    private val recipeViewModel: RecipeViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, RecipeViewModel.Factory(activity.application)).get(RecipeViewModel::class.java)
    }

    private lateinit var dataBinding: FragmentReceipeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        dataBinding  = FragmentReceipeBinding.inflate(inflater)
        dataBinding.lifecycleOwner = this
        dataBinding.recipeViewModel = recipeViewModel
        dataBinding.recipeRecycleview.layoutManager = LinearLayoutManager(activity)

        val filteredUsers = ArrayList<RecipeResult?>()
        val findItem = (activity as AppCompatActivity).toolbar.menu.findItem(R.id.action_search)
        val searchView = findItem?.actionView as SearchView
        //making the searchview consume all the toolbar when open
        searchView.maxWidth= Int.MAX_VALUE

        searchView.queryHint = "Search View Hint"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                //action while typing
                if (newText.isEmpty()){


                }else{
                    filteredUsers.clear()
                    dataBinding.recipeViewModel?.recipeList?.value!!.results.let {
                        for (recipe in it!!){
                            if (recipe!!.title.toLowerCase().contains(newText.toLowerCase())){
                                filteredUsers.add(recipe)
                            }
                        }
                    }
                    /*if (filteredUsers.isEmpty()){
                        //showing the empty textview when the list is empty
                        tvEmpty.visibility= View.VISIBLE
                    }*/

                    dataBinding.recipeViewModel?.updateDataList(filteredUsers)
                }

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                //action when type Enter
                return false
            }

        })
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.recipeRecycleview.adapter = RecipeAdapter(RecipeAdapter.OnClickListener {
            recipeViewModel.moveToRecipeDetail(it)
        })

        recipeViewModel.navigateToRecipeDetail.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                val bundle = bundleOf("recipeResult" to it)
                findNavController()
                    .navigate(R.id.recipeDetailFragment, bundle, null, null)
                recipeViewModel.doneToRecipeDetail()
            }
        })
    }
}