package com.example.savethefood.fooddetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.savethefood.EventObserver
import com.example.savethefood.databinding.FragmentFoodDetailBinding
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.repository.FoodDataRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FoodDetailFragment : Fragment() {

    private val args: FoodDetailFragmentArgs by navArgs()

    private val foodDetailViewModel: FoodDetailViewModel by viewModels()

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
        dataBinding = FragmentFoodDetailBinding.inflate(inflater).also {
            it.lifecycleOwner = this
            it.foodDetailViewModel = foodDetailViewModel

            it.recipeFab.setOnClickListener {
                foodDetailViewModel.moveToRecipeSearch(args.foodDomain)
            }

            it.deleteFab.setOnClickListener {
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

        activateObservers()
        return dataBinding.root
    }

    private fun activateObservers() {
        foodDetailViewModel.recipeFoodEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                findNavController().navigate(
                    FoodDetailFragmentDirections.actionFoodDetailFragmentToRecipeFragment(
                        it.foodTitle
                    )
                )
            }
        })

        foodDetailViewModel.foodDeleted.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                if (it) {
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireNotNull(activity), "Not deleted", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityNavigator.applyPopAnimationsToPendingTransition(requireNotNull(activity))
    }
}