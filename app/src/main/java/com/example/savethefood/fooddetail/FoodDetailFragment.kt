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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.savethefood.BaseFragment
import com.example.savethefood.EventObserver
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentFoodDetailBinding
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.repository.FoodDataRepository
import com.example.savethefood.home.FoodAdapter
import com.example.savethefood.home.HomeFragment
import com.example.savethefood.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FoodDetailFragment : BaseFragment<FoodDetailViewModel,FragmentFoodDetailBinding>() {

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
        super.init()
        dataBinding.also {
            it.foodDetailViewModel = viewModel
        }
    }

    override fun activateObservers() {

    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityNavigator.applyPopAnimationsToPendingTransition(requireNotNull(activity))
    }
}