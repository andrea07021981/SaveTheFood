package com.example.savethefood.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.R
import com.example.savethefood.component.FoodAdapter
import com.example.savethefood.component.SpacesItemDecoration
import com.example.savethefood.databinding.FragmentHomeBinding
import com.example.savethefood.viewmodel.HomeViewModel
import com.example.savethefood.viewmodel.LoginViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProviders.of(this, HomeViewModel.Factory(activity.application)).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val databinding = FragmentHomeBinding.inflate(inflater)
        databinding.lifecycleOwner = this
        databinding.homeViewModel = homeViewModel
        databinding.foodRecycleview.layoutManager = GridLayoutManager(activity, 2)
        databinding.foodRecycleview.adapter = FoodAdapter(FoodAdapter.OnClickListener {
            homeViewModel.moveToFoodDetail(it)
        })

        homeViewModel.navigateToFoodDetail.observe(this.viewLifecycleOwner, Observer {
            if (it != null) {
                //TODO move to detail fragment with the it (food) parameter
                findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToFoodDetailFragment(it))
                homeViewModel.doneToFoodDetail()
            }



            fun ExtendString(arg: String, value: Int): String {
                val another : String.(Int) -> String = {this + it}
                return arg.another(value)
            }
        })

        homeViewModel.navigateToBarcodeReader.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToBarcodeReaderFragment())
                homeViewModel.doneToBarcodeReader()
            }
        })
        return databinding.root
    }
}