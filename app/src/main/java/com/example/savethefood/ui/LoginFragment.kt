package com.example.savethefood.ui

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentLoginBinding
import com.example.savethefood.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProviders.of(this, LoginViewModel.Factory(app = activity.application)).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.custommove)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = FragmentLoginBinding.inflate(inflater)
        dataBinding.loginViewModel = loginViewModel
        dataBinding.lifecycleOwner = this

        loginViewModel.navigateToSignUpFragment.observe(this.viewLifecycleOwner, Observer {
            if (it == true) {
                this
                    .findNavController()
                    .navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
                loginViewModel.doneNavigationSignUp()
            }
        })

        //TODO check it
        //https@ //developer.android.com/guide/navigation/navigation-ui
        //Coroutines
        //https://proandroiddev.com/kotlin-coroutines-patterns-anti-patterns-f9d12984c68e

        loginViewModel.userLogged.observe(this.viewLifecycleOwner, Observer {
            if (it != null) {
                Log.d(Companion.TAG, "User logged with ${it.userEmail} and ${it.userPassword} ")
                this
                    .findNavController()
                    .navigate(LoginFragmentDirections.actionLoginFragmentToNestedNavGraph(it))
                loginViewModel.doneNavigationHome()
            }

        })
        return dataBinding.root
    }

    companion object {
        private val TAG = LoginFragment::class.java.name
    }
}