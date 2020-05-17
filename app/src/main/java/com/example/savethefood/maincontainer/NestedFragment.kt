package com.example.savethefood.maincontainer

import android.animation.Animator
import android.app.SearchManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.savethefood.R
import com.example.savethefood.R.id.foodDetailFragment
import com.example.savethefood.R.id.recipeDetailFragment
import com.example.savethefood.databinding.FragmentNestedBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_nested.*
import kotlinx.android.synthetic.main.fragment_nested.view.*


class NestedFragment : Fragment() {

    private lateinit var dataBinding: FragmentNestedBinding
    private lateinit var navController: NavController
    var toolbar: Toolbar? = null
    var navigationViewTest: NavigationView? = null
    var searchBar: LinearLayout? = null
    private val args: NestedFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        dataBinding = FragmentNestedBinding.inflate(inflater)

        //TODO check the reason of slow animation loading. Remove animation for adapter and fab
        animateTransition(
            args.params)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        toolbar = view.findViewById(R.id.toolbar);
        val fragmentContainer = view.findViewById<View>(R.id.nested_nav_graph)
        navController = Navigation.findNavController(fragmentContainer)
        // Set up ActionBar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        NavigationUI.setupActionBarWithNavController((activity as AppCompatActivity), navController, drawerLayout)
        // Set up navigation menu
        navigationViewTest = view.findViewById(R.id.navigationView)
        navigationViewTest?.setupWithNavController(navController)

        //TODO add new menu section for map and groceries
        //https://spoonacular.com/food-api/docs#Map-Ingredients-to-Grocery-Products

        //TODO add new menu for meal planning https://spoonacular.com/food-api/docs#Generate-Meal-Plan

        //TODO add menu for wines related to your recipes
        //This listener manages the nawdrawer configuration
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, bundle: Bundle? ->
            when (nd.id) {
                nc.graph.startDestination -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    appbar.toolbar.visibility = View.VISIBLE
                }
                nc.graph.findNode(foodDetailFragment)?.id,
                nc.graph.findNode(recipeDetailFragment)?.id -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    appbar.toolbar.visibility = View.GONE
                }
                else -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    //TODO change visibility of action buttons
                    appbar.toolbar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, bundle: Bundle? ->
            when (nd.id) {
                nc.graph.startDestination,
                nc.graph.findNode(R.id.recipeCookFragment)?.id,
                nc.graph.findNode(R.id.barcodeReaderFragment)?.id -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    appbar.toolbar.visibility = View.VISIBLE
                    //TODO hide for now
                    appbar.toolbar.menu.findItem(R.id.action_search).isVisible = false
                    appbar.toolbar.menu.findItem(R.id.action_filter).isVisible = false
                }
                nc.graph.findNode(foodDetailFragment)?.id,
                nc.graph.findNode(recipeDetailFragment)?.id -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    appbar.toolbar.visibility = View.GONE
                }
                else -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    appbar.toolbar.visibility = View.VISIBLE
                    appbar.toolbar.menu.findItem(R.id.action_search).isVisible = true
                    appbar.toolbar.menu.findItem(R.id.action_filter).isVisible = true
                }
            }
        }
        super.onPrepareOptionsMenu(menu)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
        val searchItem = menu.findItem(R.id.action_search)

        val searchManager =
            activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        var searchView: SearchView? = null
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Reveal animation for the view
     */
    private fun animateTransition(params: Bundle?) {
        dataBinding.lifecycleOwner = this
        Log.d("TESTTTTTTTTT", "2asd")
        dataBinding.rootLayout.addOnLayoutChangeListener { p0, p1, p2, p3, p4, p5, p6, p7, p8 ->
            // Check if the runtime version is at least Lollipop
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // get the center for the clipping circle
                val cx = dataBinding.rootLayout.width / 2
                val cy = dataBinding.rootLayout.height / 2
                val xPosition = params?.getFloat("x")?.toInt() ?: 0
                val yPosition = params?.getFloat("y")?.toInt() ?: 0

                // get the final radius for the clipping circle
                val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

                // create the animator for this view (the start radius is zero)
                val anim = ViewAnimationUtils.createCircularReveal(
                    dataBinding.rootLayout,
                    xPosition,
                    yPosition,
                    0f,
                    finalRadius
                )
                // make the view visible and start the animation
                dataBinding.rootLayout.visibility = View.VISIBLE
                anim.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                    }

                    override fun onAnimationStart(p0: Animator?) {
                        //TODO START ANIMATION FOR THE VIEW CHILDREN
                    }

                })
                anim.start()
            }
        }
    }
}