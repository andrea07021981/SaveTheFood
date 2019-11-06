package com.example.savethefood.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val databinding = FragmentHomeBinding.inflate(inflater)
        databinding.lifecycleOwner = this

        val bottomNavigationView = databinding.root.findViewById<BottomNavigationView>(R.id.bottom_home_nav)
        val childHostContainer = childFragmentManager.findFragmentById(R.id.nav_host_home_container) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNavigationView,childHostContainer.navController)
        return databinding.root
    }
}