package com.example.savethefood.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.savethefood.databinding.FragmentFoodDetailBinding
import com.example.savethefood.local.domain.FoodDomain
import com.example.savethefood.viewmodel.FoodDetailViewModel

class FoodDetailFragment : Fragment() {

    lateinit var foodSelected: FoodDomain

    private val foodDetailViewModel: FoodDetailViewModel by lazy {
        val application = requireNotNull(activity).application
        ViewModelProviders.of(this, FoodDetailViewModel.Factory(application = application, foodSelected = foodSelected))
            .get(FoodDetailViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val databinding = FragmentFoodDetailBinding.inflate(inflater)
        foodSelected = FoodDetailFragmentArgs.fromBundle(arguments!!).foodDomain
        databinding.lifecycleOwner = this
        databinding.foodDetailViewModel = foodDetailViewModel
        return databinding.root
    }
}