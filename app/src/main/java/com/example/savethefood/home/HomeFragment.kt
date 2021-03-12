package com.example.savethefood.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.*
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.databinding.FragmentHomeBinding
import com.example.savethefood.util.StorageType
import com.example.savethefood.util.configSearchView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


// TODO add weekly recipe scheduler

// TODO also add different adapter https://proandroiddev.com/understanding-kotlin-sealed-classes-65c0adad7015

// TODO add filter for foods with bottomsheetdialog fragment

const val FILTER_LIST = "filter_list"
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel: HomeViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override val layoutRes: Int
        get() = R.layout.fragment_home

    override val classTag: String
        get() = HomeFragment::class.java.simpleName

    //private val args: HomeFragmentArgs by navArgs()

    @VisibleForTesting
    fun getHomeViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO add reveal animation from fab
        /*exitTransition = MaterialFadeThrough().apply {
            addTarget(R.id.rootLayout)
            duration = 500
        }*/
    }

    override fun init() {
        with(dataBinding) {
            homeViewModel = viewModel
            foodRecycleview.layoutManager = LinearLayoutManager(activity)
            foodRecycleview.adapter =
                FoodAdapter(FoodAdapter.OnClickListener { food ->
                    viewModel.moveToFoodDetail(food)
                })
            //it.foodRecycleview.scheduleLayoutAnimation()
            arguments?.takeIf { it.containsKey(FILTER_LIST) }?.apply {
                viewModel.updateIndex(getSerializable(FILTER_LIST) as StorageType)
            }
        }
    }

    override fun activateObservers() {
        viewModel.detailFoodEvent.observe(viewLifecycleOwner, ::navigateTo) // TODO is it better to keep the EventObserver?
        viewModel.addFoodEvent.observe(viewLifecycleOwner, ::navigateTo)
    }

    override fun <T> navigateTo(event: Event<T>?) {
        event?.let {
            if (it.hasBeenHandled) return
            val content = it.getContentIfNotHandled()
            when (it.peekContent()) {
                is Unit -> findNavController()
                    .navigate(R.id.addFoodFragment)
                is FoodDomain -> {
                    with(dataBinding.foodRecycleview) {
                        val foodImageView = findViewById<ImageView>(R.id.food_imageview)
                        val foodTextView = findViewById<TextView>(R.id.food_textview)
                        val extras = FragmentNavigatorExtras(
                            foodImageView to "foodImage",
                            foodTextView to "foodTitle"
                        )
                        val bundle = bundleOf("foodDomain" to content)
                        findNavController()
                            .navigate(R.id.foodDetailFragment, bundle, null, extras)
                    }
                }
            }
        }
    }
}