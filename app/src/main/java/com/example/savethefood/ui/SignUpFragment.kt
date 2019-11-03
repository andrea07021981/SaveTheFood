package com.example.savethefood.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.savethefood.databinding.FragmentSignupBinding
import com.example.savethefood.viewmodel.SignUpViewModel

class SignUpFragment : Fragment() {

    private val signUpViewModel: SignUpViewModel by lazy {
        ViewModelProviders.of(this).get(SignUpViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val databinding = FragmentSignupBinding.inflate(inflater)
        databinding.signupViewModel = signUpViewModel
        databinding.lifecycleOwner = this

        /*signUpViewModel.errorUserName.observe(this, Observer {
            when (it) {
                true -> databinding.etUsername.error = "Username is mandatory"
                else -> databinding.etUsername.error = null
            }
        })
        signUpViewModel.errorEmail.observe(this, Observer {
            when (it) {
                true -> databinding.etUsername.error = "Email is mandatory"
                else -> databinding.etUsername.error = null
            }
        })
        signUpViewModel.errorPassword.observe(this, Observer {
            when (it) {
                true -> databinding.etUsername.error = "Password is mandatory"
                else -> databinding.etUsername.error = null
            }
        })*/
        return databinding.root
    }
}