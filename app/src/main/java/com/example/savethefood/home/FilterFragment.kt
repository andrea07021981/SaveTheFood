package com.example.savethefood.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.savethefood.databinding.FragmentFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFilterBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        return binding.root
    }
}