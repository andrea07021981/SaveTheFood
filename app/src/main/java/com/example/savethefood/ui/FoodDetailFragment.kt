package com.example.savethefood.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.ActivityNavigator
import androidx.transition.TransitionInflater
import com.example.savethefood.databinding.FragmentFoodDetailBinding
import com.example.savethefood.local.domain.FoodDomain
import com.example.savethefood.viewmodel.FoodDetailViewModel

class FoodDetailFragment : Fragment() {

    lateinit var foodSelected: FoodDomain

    private val foodDetailViewModel: FoodDetailViewModel by lazy {
        val application = requireNotNull(activity).application
        ViewModelProviders.of(this, FoodDetailViewModel.Factory(application = application, foodSelected = foodSelected))
            .get(FoodDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        val dataBinding = FragmentFoodDetailBinding.inflate(inflater)
        foodSelected = FoodDetailFragmentArgs.fromBundle(arguments!!).foodDomain
        dataBinding.lifecycleOwner = this
        dataBinding.foodDetailViewModel = foodDetailViewModel

        dataBinding.recipeFab.setOnClickListener {

        }

        dataBinding.deleteFab.setOnClickListener {
            AlertDialog.Builder(activity!!)
                .setCancelable(false)
                .setTitle("Attention")
                .setMessage("Would you like to delete ${foodDetailViewModel.food.value?.foodTitle}")
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                    foodDetailViewModel.deleteFood()
                    dialogInterface.dismiss()
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                .create()
                .show()
        }
        return dataBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityNavigator.applyPopAnimationsToPendingTransition(requireNotNull(activity))
    }
}