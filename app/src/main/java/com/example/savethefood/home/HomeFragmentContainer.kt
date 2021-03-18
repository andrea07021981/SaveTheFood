package com.example.savethefood.home

import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.savethefood.BaseFragment
import com.example.savethefood.EventObserver
import com.example.savethefood.FragmentCallback
import com.example.savethefood.R
import com.example.savethefood.databinding.CustomTabLayoutBinding
import com.example.savethefood.databinding.FragmentHomeContainerBinding
import com.example.savethefood.util.StorageType
import com.example.savethefood.util.configSearchView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_tab_layout.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragmentContainer : BaseFragment<HomeViewModel, FragmentHomeContainerBinding>(),
    FragmentCallback {


    override val viewModel by viewModels<HomeViewModel>()

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

        viewModel.errorData.observe(viewLifecycleOwner, EventObserver {
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
                Log.d(classTag, "Value searched: $it")
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