package com.example.savethefood

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.example.savethefood.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        drawerLayout = binding.drawerLayout
        val navController = findNavController(R.id.nav_host_fragment)
        val navigationView = this.findViewById<NavigationView>(R.id.navView)

        //This forces the drawer menu in home and avoid the back button navigation
        //Param topleveldest contains the set of destinations by id considered at the top level of your information hierarchy. The Up button will not be displayed when on these destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment,
            R.id.recipeFragment,
            R.id.mapFragment
        ), drawerLayout)

        // prevent nav gesture if not on start destination
        //TODO BAck navigation doesn't work
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, _: Bundle? ->
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            when (nd.id) {
                nc.graph.findNode(R.id.homeFragment)?.id -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    appbar.toolbar.visibility = View.VISIBLE
                }
                nc.graph.findNode(R.id.foodFragment)?.id,
                nc.graph.findNode(R.id.recipeCookFragment)?.id,
                nc.graph.findNode(R.id.recipeFragment)?.id,
                nc.graph.findNode(R.id.mapFragment)?.id -> {
                    appbar.toolbar.visibility = View.VISIBLE
                }
                else -> {
                    appbar.toolbar.visibility = View.GONE
                }
            }
        }

        createDrawerAnimation(navigationView, navController)
        NavigationUI.setupWithNavController(binding.navView, navController)
    }

    /**
     * Manage the effect of the open/close drawer
     */
    private fun createDrawerAnimation(
        navigationView: NavigationView,
        navController: NavController
    ) {
        //TODO ORGANIZE CODE
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        (toolbar as Toolbar).setNavigationOnClickListener { v ->
            when (navController.currentDestination?.id) {
                navController.graph.findNode(R.id.foodFragment)?.id,
                navController.graph.findNode(R.id.recipeCookFragment)?.id,
                navController.graph.findNode(R.id.recipeDetailFragment)?.id -> {
                    navController.popBackStack()
                }
                else -> {
                    if (drawerLayout.isDrawerOpen(navigationView)) {
                        drawerLayout.closeDrawer(navigationView)
                    } else {
                        drawerLayout.openDrawer(navigationView, true)
                    }
                }
            }
        }

        drawerLayout.setScrimColor(Color.TRANSPARENT)
        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerSlide(
                drawerView: View,
                slideOffset: Float
            ) {

                // Scale the View based on current slide offset
                val diffScaledOffset = slideOffset * (1 - 0.5f)
                val offsetScale = 1 - diffScaledOffset
                main_layout.scaleX = offsetScale
                main_layout.scaleY = offsetScale

                // Translate the View, accounting for the scaled width
                val xOffset = drawerView.width * slideOffset
                val xOffsetDiff: Float = main_layout.width * diffScaledOffset / 2
                val xTranslation = xOffset - xOffsetDiff
                main_layout.translationX = xTranslation
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
