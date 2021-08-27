package com.example.savethefood.home

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.savethefood.BaseFragment
import com.example.savethefood.FragmentCallback
import com.example.savethefood.R
import com.example.savethefood.constants.Constants.BUNDLE_KEY
import com.example.savethefood.constants.Constants.REQUEST_KEY
import com.example.savethefood.databinding.CustomTabLayoutBinding
import com.example.savethefood.databinding.FragmentHomeContainerBinding
import com.example.savethefood.shared.utils.StorageType
import com.example.savethefood.shared.viewmodel.HomeViewModel
import com.example.savethefood.util.configSearchView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragmentContainer : BaseFragment<HomeViewModel, FragmentHomeContainerBinding>(),
    FragmentCallback {

    override val viewModel: HomeViewModel by viewModel()

    override val layoutRes: Int
        get() = R.layout.fragment_home_container

    override val classTag: String
        get() = HomeFragment::class.java.simpleName

    override fun init() {
        with(dataBinding) {
            viewPager.adapter = HomeFragmentContainerAdapter(childFragmentManager, lifecycle)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                val tabCustomView = CustomTabLayoutBinding.inflate(layoutInflater)
                tabCustomView.titleTextView.text = StorageType.values()[position].type
                tab.customView = tabCustomView.root
                tab.tag = StorageType.values()[position].type
            }.attach()
        }
        setHasOptionsMenu(true)
    }

    override fun activateObservers() {
        viewModel.listByStorageType.observe(viewLifecycleOwner) {
            if (it != null) {
                for ((index, value) in it) {
                    val linearLayout = dataBinding.tabLayout.getTabAt(index.ordinal)
                        ?.customView as LinearLayout
                    CustomTabLayoutBinding.bind(linearLayout).countTextView.text =
                        value.toString()
                }
            }
        }

        viewModel.errorData.observe(viewLifecycleOwner, com.example.savethefood.shared.utils.EventObserver {
            Snackbar.make(dataBinding.root, getString(R.string.no_data), Snackbar.LENGTH_LONG)
                .show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar, menu)
        with(menu.findItem(R.id.action_search)) {
            configSearchView(
                requireNotNull(activity),
                "Search Food"
            ) {
                viewModel.updateDataList(it)
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

    override fun onResume() {
        super.onResume()
        setFragmentResultListener(REQUEST_KEY) { _, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result = bundle.get(BUNDLE_KEY) as com.example.savethefood.shared.utils.FoodOrder
            viewModel.updateDataList(result)
        }
    }
}