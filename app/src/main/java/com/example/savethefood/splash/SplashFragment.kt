package com.example.savethefood.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private val splashViewModel by viewModels<SplashViewModel>()

    private lateinit var dataBinding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        dataBinding = FragmentSplashBinding.inflate(inflater);
        dataBinding.splashViewModel = splashViewModel
        dataBinding.lifecycleOwner = this
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        splashViewModel.loginEvent.observe(this.viewLifecycleOwner, Observer {
            it.let {
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
            }
        })
    }
}