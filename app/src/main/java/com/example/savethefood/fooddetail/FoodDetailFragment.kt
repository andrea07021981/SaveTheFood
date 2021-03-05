package com.example.savethefood.fooddetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.savethefood.BaseFragment
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentFoodDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FoodDetailFragment : BaseFragment<FoodDetailViewModel, FragmentFoodDetailBinding>() {

    private val args: FoodDetailFragmentArgs by navArgs()

    override val viewModel by viewModels<FoodDetailViewModel>()

    override val layoutRes: Int
        get() = R.layout.fragment_food_detail

    override val classTag: String
        get() = FoodDetailFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun init() {
        with(dataBinding) {
            foodDetailViewModel = viewModel
        }

        //TODO change expiring color based on how many days (gree, yellow and red)
    }

    override fun activateObservers() {
        viewModel.food.observe(viewLifecycleOwner, {
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = it.title
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityNavigator.applyPopAnimationsToPendingTransition(requireNotNull(activity))
    }
}