package com.example.savethefood.login

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentSplashBinding
import kotlinx.android.synthetic.main.fragment_splash.view.*

class SplashFragment : Fragment() {

    private val splashViewModel: SplashViewModel by lazy {
        val application = requireNotNull(this.activity).application
        ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val databinding = FragmentSplashBinding.inflate(inflater);
        databinding.splashViewModel = splashViewModel
        databinding.setLifecycleOwner(this)


        return databinding.root
    }
}