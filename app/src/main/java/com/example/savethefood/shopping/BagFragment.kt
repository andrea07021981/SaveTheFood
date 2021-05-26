package com.example.savethefood.shopping

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.*
import com.example.savethefood.FragmentCallback
import com.example.savethefood.addfood.SearchableFragment
import com.example.savethefood.databinding.FragmentBagBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BagFragment : BaseFragment<BagViewModel, FragmentBagBinding>(), FragmentCallback {

    override val viewModel by viewModels<BagViewModel>()

    override val layoutRes: Int
        get() = R.layout.fragment_bag

    override val classTag: String
        get() = BagFragment::class.java.simpleName

    val errorName = MutableLiveData<Boolean>()
    val errorQuantity = MutableLiveData<Boolean>()

    override fun init() {
        with(dataBinding) {
            bagViewModel = viewModel
            with(bagRecycleView) {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = BagAdapter(
                    BaseAdapter.BaseClickListener {
                        // TODO open the item details
                    }
                )
            }
        }
    }

    override fun activateObservers() {
        viewModel.bagDetailEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(BagFragmentDirections.actionBagFragmentToBagDetail())
        })
    }

    override fun onAddClicked(view: View) {
        viewModel.navigateToBadItemDetail()
    }
}