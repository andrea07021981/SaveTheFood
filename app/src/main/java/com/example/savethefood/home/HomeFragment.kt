package com.example.savethefood.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.*
import com.example.savethefood.data.succeeded
import com.example.savethefood.databinding.FragmentHomeBinding
import com.example.savethefood.util.configSearchView
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


// TODO Home must host the tablayout. Use Homeviewmodel for all tabs and switchMap to filter by StorageType
// https://www.javatpoint.com/android-tablayout
// TODO add weekly recipe scheduler

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
        super.init()
        dataBinding.also {
            it.homeViewModel = viewModel
            it.foodRecycleview.layoutManager = LinearLayoutManager(activity)
            setHasOptionsMenu(true)
            it.foodRecycleview.adapter =
                FoodAdapter(FoodAdapter.OnClickListener { food ->
                    viewModel.moveToFoodDetail(food)
                })
            //it.foodRecycleview.scheduleLayoutAnimation()
        }

        //Set tittle and Animate the fab
        with(requireNotNull(activity) as MainActivity) {
            btn_add.apply {
            animate()
                .setDuration(1000.toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        this@apply.show()
                    }
                }).start()
            }
        }
    }

    override fun activateObservers() {
        viewModel.detailFoodEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                val foodImageView =
                    dataBinding.foodRecycleview.findViewById<ImageView>(R.id.food_imageview)
                val foodTextview =
                    dataBinding.foodRecycleview.findViewById<TextView>(R.id.food_textview)
                val extras = FragmentNavigatorExtras(
                    foodImageView to "foodImage",
                    foodTextview to "foodTitle"
                )
                val bundle = bundleOf("foodDomain" to it)
                findNavController()
                    .navigate(R.id.foodDetailFragment, bundle, null, extras)
            }
        })

        viewModel.barcodeFoodEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                FragmentIntentIntegrator(this).initiateScan()
            }
        })

        viewModel.addFoodEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddFoodFragment())
            }
        })

        viewModel.newFoodFoodEvent.observe(viewLifecycleOwner, Observer {
            if (it.succeeded) {
                //TODO show a dialog with some info, editable and with the date if not present in barcode
                Log.d(TAG, "Added")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        //TODO forced value, emulator can't read barcode
        //041631000564
        result?.let {
            viewModel.getApiFoodDetails(it.contents ?: "041631000564")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    inner class FragmentIntentIntegrator(
        private val fragment: Fragment
    ) : IntentIntegrator(fragment.activity) {

        override fun startActivityForResult(intent: Intent, code: Int) {
            fragment.startActivityForResult(intent, code)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchItem.configSearchView(
            requireNotNull(activity),
            "Search Food"
        ) {
            Log.d(TAG, "Value searched: $it")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onAddClicked(view: View) {
        viewModel.navigateToAddFood()
    }
}