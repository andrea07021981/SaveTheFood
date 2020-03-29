package com.example.savethefood.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentSplashBinding
import com.example.savethefood.viewmodel.SplashViewModel

class SplashFragment : Fragment() {

    private val splashViewModel: SplashViewModel by lazy {
        val application = requireNotNull(this.activity).application
        ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }

    private lateinit var dataBinding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentSplashBinding.inflate(inflater);
        dataBinding.splashViewModel = splashViewModel
        dataBinding.lifecycleOwner = this
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        splashViewModel.navigateToLogin.observe(this.viewLifecycleOwner, Observer {
            if (it == true) {
                val extras = FragmentNavigatorExtras(
                    dataBinding.chefImageview to "chef_imageview"
                )
                this
                    .findNavController()
                    .navigate(R.id.action_splashFragment_to_loginFragment,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.splashFragment,
                                true).build(),
                        extras)
                splashViewModel.doneNavigating()
            }
        })
    }
}