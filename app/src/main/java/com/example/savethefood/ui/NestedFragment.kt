package com.example.savethefood

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.transition.TransitionManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_nested.*
import androidx.core.view.MenuItemCompat.getActionView
import android.content.Context.SEARCH_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.app.SearchManager
import android.content.Context



class NestedFragment : Fragment() {

    var toolbar: Toolbar? = null
    var navigationViewTest: NavigationView? = null
    var searchBar: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_nested, container,
            false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        toolbar = view.findViewById(R.id.toolbar);
        val fragmentContainer = view.findViewById<View>(R.id.nested_nav_graph)
        val navController = Navigation.findNavController(fragmentContainer)
        // Set up ActionBar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        NavigationUI.setupActionBarWithNavController((activity as AppCompatActivity), navController, drawerLayout)
        // Set up navigation menu
        navigationViewTest = view.findViewById(R.id.navigationView)
        navigationViewTest?.setupWithNavController(navController)
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
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(activity?.getComponentName()))

        super.onCreateOptionsMenu(menu, inflater)
    }
}