package com.example.savethefood.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionInflater
import com.example.savethefood.R
import com.example.savethefood.component.FoodAdapter
import com.example.savethefood.databinding.FragmentHomeBinding
import com.example.savethefood.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, HomeViewModel.Factory(activity.application)).get(HomeViewModel::class.java)
    }

    private lateinit var dataBinding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        dataBinding = FragmentHomeBinding.inflate(inflater)
        dataBinding.lifecycleOwner = this
        dataBinding.homeViewModel = homeViewModel
        dataBinding.foodRecycleview.layoutManager = GridLayoutManager(activity, 2)
        return dataBinding.root
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.foodRecycleview.adapter = FoodAdapter(FoodAdapter.OnClickListener {
            homeViewModel.moveToFoodDetail(it)
        })

        homeViewModel.navigateToFoodDetail.observe(this.viewLifecycleOwner, Observer {
            if (it != null) {
                val foodImageView =
                    dataBinding.foodRecycleview.findViewById<ImageView>(R.id.food_imageview)
                val foodTextview =
                    dataBinding.foodRecycleview.findViewById<TextView>(R.id.food_textview)
                val extras = FragmentNavigatorExtras(
                    foodImageView to "foodImage",
                    foodTextview to "foodTitle")
                val bundle = bundleOf("foodDomain" to it)
                findNavController()
                    .navigate(R.id.foodDetailFragment, bundle, null, extras)
                homeViewModel.doneToFoodDetail()
            }
        })

        homeViewModel.navigateToBarcodeReader.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToBarcodeReaderFragment())
                homeViewModel.doneToBarcodeReader()
            }
        })
    }
}