package com.example.savethefood.shopping

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.savethefood.BaseFragment
import com.example.savethefood.EventObserver
import com.example.savethefood.Notifier
import com.example.savethefood.R
import com.example.savethefood.addfood.AddFoodFragment
import com.example.savethefood.addfood.SearchableFragment
import com.example.savethefood.constants.Constants
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodItem
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
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        childFragmentManager.setFragmentResultListener(Constants.REQUEST_KEY, viewLifecycleOwner) { _, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result = bundle.get(Constants.BUNDLE_KEY) as FoodItem
            viewModel.updateFood(result)}
    }
    override fun activateObservers() {
        viewModel.openFoodTypeDialog.observe(viewLifecycleOwner, EventObserver {
            SearchableFragment().show(childFragmentManager, classTag)
        })

        viewModel.saveFoodEvent.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    // TODO Add the deep link ass add food
                    findNavController().popBackStack()
                }
                is Result.ExError, is Result.Error -> Toast.makeText(
                    context,
                    "Error saving food in bag",
                    Toast.LENGTH_LONG
                ).show()
                else -> Unit
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_barcode, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.barcode) {
            //viewModel.navigateToBarcodeReader()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}