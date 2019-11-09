package com.example.savethefood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_nested.*
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*



class NestedFragment : Fragment() {

    var toolbar: Toolbar? = null
    var navigationViewTest: NavigationView? = null

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
}