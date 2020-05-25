package com.example.savethefood.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.savethefood.databinding.FragmentFoodBinding

class FoodFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var databinding = FragmentFoodBinding.inflate(inflater)
        return databinding.root
    }
}

