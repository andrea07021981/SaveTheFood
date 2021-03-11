package com.example.savethefood.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.*
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.succeeded
import com.example.savethefood.databinding.FragmentHomeBinding
import com.example.savethefood.util.configSearchView
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


// TODO Home must host the tablayout. Use Homeviewmodel for all tabs and switchMap to filter by StorageType
// https://www.javatpoint.com/android-tablayout
// TODO add weekly recipe scheduler

// TODO also add different adapter https://proandroiddev.com/understanding-kotlin-sealed-classes-65c0adad7015

// TODO add filter for foods with bottomsheetdialog fragment

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(), FragmentCallback {

    override val viewModel by viewModels<HomeViewModel>()

    override val layoutRes: Int
        get() = R.layout.fragment_home

    override val classTag: String
        get() = HomeFragment::class.java.simpleName

    companion object {
        val TAG: String = HomeFragment::class.java.simpleName
    }

    private val args: HomeFragmentArgs by navArgs()

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
            setHasOptionsMenu(true)
            foodRecycleview.adapter =
                FoodAdapter(FoodAdapter.OnClickListener { food ->
                    viewModel.moveToFoodDetail(food)
                })
            //it.foodRecycleview.scheduleLayoutAnimation()
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
                    .navigate(HomeFragmentDirections.actionHomeFragmentToAddFoodFragment())
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar, menu)
        with(menu.findItem(R.id.action_search)) {
            configSearchView(
                requireNotNull(activity),
                "Search Food"
            ) {
                Log.d(TAG, "Value searched: $it")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (NavigationUI.onNavDestinationSelected(item, findNavController())
                || super.onOptionsItemSelected(item))
    }

    override fun onAddClicked(view: View) {
        viewModel.navigateToAddFood()
    }
}