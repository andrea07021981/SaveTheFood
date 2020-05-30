package com.example.savethefood.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.savethefood.data.source.repository.FoodDataRepository
import com.example.savethefood.databinding.FragmentFoodBinding

class FoodFragment : Fragment() {

    private val foodViewModel by viewModels<FoodViewmodel> {
        FoodViewmodel.FoodViewModelFactory(FoodDataRepository.getRepository(requireActivity().application))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var dataBinding = FragmentFoodBinding.inflate(inflater)
        dataBinding.lifecycleOwner = this
        dataBinding.foodViewModel = foodViewModel
        return dataBinding.root
    }
}

