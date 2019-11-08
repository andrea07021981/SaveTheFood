package com.example.savethefood.ui

import android.R
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.savethefood.databinding.FragmentHomeBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_home.*
import com.example.savethefood.R.menu.home_appbar_items
import com.example.savethefood.R.menu.receipe_appbar_items
import com.example.savethefood.R.drawable.*
import com.example.savethefood.R.mipmap.*
import com.example.savethefood.component.BottomNavigationDrawerFragment

class HomeFragment : Fragment() {

    private var currentFabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
    private lateinit var addVisibilityChanged: FloatingActionButton.OnVisibilityChangedListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val databinding = FragmentHomeBinding.inflate(inflater)
        databinding.lifecycleOwner = this


        setHasOptionsMenu(true)

        return databinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(com.example.savethefood.R.menu.home_appbar_items, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = (requireActivity() as AppCompatActivity)
        activity.setSupportActionBar(bottom_home_bar)

        addVisibilityChanged = object : FloatingActionButton.OnVisibilityChangedListener() {
            override fun onShown(fab: FloatingActionButton?) {
                super.onShown(fab)
            }
            override fun onHidden(fab: FloatingActionButton?) {
                super.onHidden(fab)
                bottom_home_bar.toggleFabAlignment()
                bottom_home_bar.replaceMenu(
                    if(currentFabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) receipe_appbar_items
                    else home_appbar_items
                )
                fab?.setImageDrawable(
                    if(currentFabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) activity.getDrawable(
                        email_white_24dp)
                    else activity.getDrawable(email_white_24dp)
                )
                fab?.show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item!!.itemId) {
            R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                activity?.supportFragmentManager?.let { bottomNavDrawerFragment.show(it, bottomNavDrawerFragment.tag) }
            }
            com.example.savethefood.R.id.foodFragment -> {
                fab.hide(addVisibilityChanged)
                invalidateOptionsMenu(activity)
                bottom_home_bar.navigationIcon = if (bottom_home_bar.navigationIcon != null) null
                else activity?.getDrawable(
                    baseline_menu_white)
                (childFragmentManager.fragments.get(0) as NavHostFragment).navController.navigate(FoodFragmentDirections.actionFoodFragmentToReceipeFragment())
            }
        }
        return true
    }

    private fun BottomAppBar.toggleFabAlignment() {
        currentFabAlignmentMode = fabAlignmentMode
        fabAlignmentMode = currentFabAlignmentMode.xor(1)
    }
}