package com.example.savethefood.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.savethefood.databinding.FragmentReceipeBinding

class ReceipeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding  = FragmentReceipeBinding.inflate(inflater)
        dataBinding.lifecycleOwner = this
        return dataBinding.root
    }
}