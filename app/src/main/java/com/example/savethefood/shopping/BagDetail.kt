package com.example.savethefood.shopping

import androidx.fragment.app.viewModels
import com.example.savethefood.BaseFragment
import com.example.savethefood.EventObserver
import com.example.savethefood.R
import com.example.savethefood.addfood.SearchableFragment
import com.example.savethefood.databinding.FragmentBagBinding
import com.example.savethefood.databinding.FragmentBagDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BagDetail : BaseFragment<BagViewModel, FragmentBagDetailBinding>() {

    override val viewModel: BagViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override val layoutRes: Int
        get() = R.layout.fragment_bag_detail

    override val classTag: String
        get() = BagDetail::class.java.simpleName

    override fun init() {
        with(dataBinding) {
            bagViewModel = viewModel
        }
    }
    override fun activateObservers() {
        viewModel.openFoodTypeDialog.observe(viewLifecycleOwner, EventObserver {
            SearchableFragment().show(childFragmentManager, classTag)
        })
    }
}