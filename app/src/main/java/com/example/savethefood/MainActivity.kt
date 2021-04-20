package com.example.savethefood

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.savethefood.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

const val FAB_DURATION = 500
internal interface FragmentCallback {
    fun onAddClicked(view: View)
}

//TODO add meal plan for next weeks in menu drawer
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var binding:  ActivityMainBinding
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.btnAdd.setOnClickListener {
            val navHostFragment = supportFragmentManager.primaryNavigationFragment as NavHostFragment
            val currentFragment = navHostFragment.childFragmentManager.primaryNavigationFragment as Fragment
            if (currentFragment is FragmentCallback) {
                currentFragment.onAddClicked(binding.btnAdd)
                binding.btnAdd.isExpanded = true
            }
        }
        setUpNavigation(binding)

        // Init the notifications
        Notifier.init(this)
    }

    private fun setUpNavigation(binding: ActivityMainBinding) {
        navController = findNavController(R.id.nav_host_fragment) as NavHostController
        val graphs = {
            setOf(
                R.id.splashFragment, R.id.loginFragment, R.id.signUpFragment, R.id.homeFragmentContainer, R.id.recipeFragment, R.id.mapFragment
            )
        }
        appBarConfiguration = AppBarConfiguration(graphs())

        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragmentContainer, R.id.mapFragment, R.id.recipeFragment -> {
                    binding.cordinatorBottom.visibility = View.VISIBLE
                }
                else -> binding.cordinatorBottom.visibility = View.GONE
            }
            animateFab(destination)
            binding.toolbar.updateVisibility(destination.id)
        }

        // The setupWithNavController should be use only with navdrawer and bottom nav
        // With toolbar, The start destination of your navigation graph is considered the only top
        // level destination. On all other destinations, the ActionBar will show the Up button
        // we want to use the custom top level destination we must pass the appBarConfiguration
        binding.navView.setupWithNavController(navController)
    }

    private fun animateFab(destination: NavDestination) {
        binding.btnAdd.apply {
            animate()
                .setDuration(FAB_DURATION.toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        if (destination.id == R.id.homeFragmentContainer) {
                            binding.navView.menu.getItem(2).isVisible = true
                            this@apply.show()
                        } else {
                            this@apply.hide()
                            binding.navView.menu.getItem(2).isVisible = false
                        }
                    }
                }).start()
        }
    }

    private fun Toolbar.updateVisibility(id: Int) {
        visibility = when (id) {
            R.id.splashFragment,
            R.id.loginFragment,
            R.id.signUpFragment,
            R.id.recipeDetailFragment -> View.GONE
            else -> View.VISIBLE
        }
    }

    // TODO add nested chart to solve the back
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
