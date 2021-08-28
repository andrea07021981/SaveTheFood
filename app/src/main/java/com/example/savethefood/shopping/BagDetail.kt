package com.example.savethefood.shopping

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.savethefood.BaseFragment
import com.example.savethefood.R
import com.example.savethefood.addfood.SearchableFragment
import com.example.savethefood.constants.Constants
import com.example.savethefood.databinding.FragmentBagDetailBinding
import com.example.savethefood.recipedetail.RecipeDetailFragmentArgs
import com.example.savethefood.shared.data.domain.FoodItem
import com.example.savethefood.shared.viewmodel.BagDetailViewModel
import com.example.savethefood.shared.viewmodel.FoodDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@AndroidEntryPoint
class BagDetail : BaseFragment<BagDetailViewModel, FragmentBagDetailBinding>() {

    private val args: BagDetailArgs by navArgs()

    override val viewModel: BagDetailViewModel by stateViewModel(
        state = { args.bagDetail },
        clazz = BagDetailViewModel::class
    )

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
        viewModel.openFoodTypeDialog.observe(viewLifecycleOwner, com.example.savethefood.shared.utils.EventObserver {
            //SearchableFragment().show(childFragmentManager, classTag)
            // TODO add a spinner for grocery category and for the priority of a specific food
        })

        viewModel.saveFoodEvent.observe(viewLifecycleOwner) {
            when (it) {
                is com.example.savethefood.shared.data.Result.Success -> {
                    // TODO Add the deep link ass add food
                    findNavController().popBackStack()
                }
                is com.example.savethefood.shared.data.Result.ExError,
                is com.example.savethefood.shared.data.Result.Error -> Toast.makeText(
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