package com.example.savethefood.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.example.savethefood.databinding.FragmentFoodDetailBinding
import com.example.savethefood.local.domain.FoodDomain
import com.example.savethefood.viewmodel.FoodDetailViewModel
import com.google.android.material.appbar.CollapsingToolbarLayout

class FoodDetailFragment : Fragment() {

    private val foodDetailViewModel: FoodDetailViewModel by lazy {
        val application = requireNotNull(activity).application
        ViewModelProvider(this, FoodDetailViewModel.Factory(application = application, foodSelected = foodSelected))
            .get(FoodDetailViewModel::class.java)
    }

    private lateinit var foodSelected: FoodDomain
    private lateinit var dataBinding: FragmentFoodDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        dataBinding = FragmentFoodDetailBinding.inflate(inflater)
        foodSelected = FoodDetailFragmentArgs.fromBundle(requireArguments()).foodDomain
        dataBinding.lifecycleOwner = this
        dataBinding.foodDetailViewModel = foodDetailViewModel
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.recipeFab.setOnClickListener {
            foodDetailViewModel.moveToRecipeSearch(foodSelected)
        }

        dataBinding.foodDetailViewModel!!.navigateToRecipeSearch.observe(this.viewLifecycleOwner, Observer {
            it.let {
                findNavController().navigate(FoodDetailFragmentDirections.actionFoodDetailFragmentToRecipeFragment(it.foodId))
                foodDetailViewModel.doneRecipeSearch()
            }
        })
        dataBinding.deleteFab.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                .setCancelable(false)
                .setTitle("Attention")
                .setMessage("Would you like to delete ${foodDetailViewModel.food.value?.foodTitle}")
                .setPositiveButton("OK") { dialogInterface, _ ->
                    foodDetailViewModel.deleteFood()
                    dialogInterface.dismiss()
                }
                .setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
                .create()
                .show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityNavigator.applyPopAnimationsToPendingTransition(requireNotNull(activity))
    }
}