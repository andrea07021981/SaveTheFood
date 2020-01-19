package com.example.savethefood.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.component.RecipeAdapter
import com.example.savethefood.databinding.FragmentReceipeBinding
import com.example.savethefood.viewmodel.RecipeViewModel

class RecipeFragment : Fragment() {

    private val recipeViewModel: RecipeViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProviders.of(this, RecipeViewModel.Factory(activity.application)).get(RecipeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding  = FragmentReceipeBinding.inflate(inflater)
        dataBinding.lifecycleOwner = this
        dataBinding.recipeViewModel = recipeViewModel
        dataBinding.recipeRecycleview.layoutManager = LinearLayoutManager(activity)
        dataBinding.recipeRecycleview.adapter = RecipeAdapter(RecipeAdapter.OnClickListener {
            //TODO open recipe details
        })
        return dataBinding.root
    }
}