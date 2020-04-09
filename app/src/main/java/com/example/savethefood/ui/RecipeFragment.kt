package com.example.savethefood.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadLayoutAnimation
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.R
import com.example.savethefood.component.RecipeAdapter
import com.example.savethefood.databinding.FragmentReceipeBinding
import com.example.savethefood.viewmodel.RecipeViewModel

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