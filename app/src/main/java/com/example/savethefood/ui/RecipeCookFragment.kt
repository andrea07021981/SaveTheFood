package com.example.savethefood.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.component.StepCookAdapter
import com.example.savethefood.databinding.FragmentRecipeCookBinding
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.viewmodel.RecipeCookViewModel

class RecipeCookFragment : Fragment() {

    private val recipeCookViewModel: RecipeCookViewModel by lazy {
        val application = requireNotNull(activity).application
        ViewModelProvider(this, RecipeCookViewModel.Factory(application = application, recipe = recipeInfoSelected))
            .get(RecipeCookViewModel::class.java)
    }

    private lateinit var recipeInfoSelected: RecipeInfoDomain
    private lateinit var dataBinding: FragmentRecipeCookBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        dataBinding = FragmentRecipeCookBinding.inflate(inflater)
        recipeInfoSelected = RecipeCookFragmentArgs.fromBundle(requireArguments()).recipeInfoDomain
        dataBinding.lifecycleOwner = this
        dataBinding.recipeCookViewModel = recipeCookViewModel

        dataBinding.stepRecycleView.layoutManager = LinearLayoutManager(activity)
        dataBinding.stepRecycleView.adapter = StepCookAdapter(StepCookAdapter.OnStepClickListener {
        })
        return dataBinding.root
    }

}