package com.example.savethefood.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.example.savethefood.constants.Constants.BUNDLE_KEY
import com.example.savethefood.constants.Constants.REQUEST_KEY
import com.example.savethefood.databinding.FragmentFilterBinding
import com.example.savethefood.shared.utils.EventObserver
import com.example.savethefood.shared.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

// TODO move the chip common style in styles.xml
@AndroidEntryPoint
class FilterFragment : BottomSheetDialogFragment() {

    private val viewModel: HomeViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFilterBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        viewModel.listOrderEvent.observe(viewLifecycleOwner, EventObserver {
            //dismiss() Without dismiss we can see the event immediately
            setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to it))
        })

        return binding.root
    }
}