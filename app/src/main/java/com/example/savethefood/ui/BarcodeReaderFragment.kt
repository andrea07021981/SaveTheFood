package com.example.savethefood.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.savethefood.databinding.FragmentBarcodereaderBinding
import com.example.savethefood.viewmodel.BarcodeReaderViewModel
import com.example.savethefood.viewmodel.HomeViewModel

class BarcodeReaderFragment : Fragment() {

    private val barcodeReaderViewModel: BarcodeReaderViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProviders.of(this, BarcodeReaderViewModel.Factory(activity.application)).get(BarcodeReaderViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = FragmentBarcodereaderBinding.inflate(inflater)
        dataBinding.lifecycleOwner = this
        dataBinding.barcodeReaderViewModel = barcodeReaderViewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}