package com.example.savethefood.ui

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentLoginBinding
import com.example.savethefood.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    companion object {
        private val TAG = LoginFragment::class.java.name
    }

    private val loginViewModel by viewModels<LoginViewModel> {
        val activity = requireNotNull(this.activity)
        LoginViewModel.Factory(app = activity.application)
    }

    private lateinit var dataBinding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.custommove)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        dataBinding = FragmentLoginBinding.inflate(inflater)
        dataBinding.loginViewModel = loginViewModel
        dataBinding.lifecycleOwner = this
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
            } else if (loginViewModel.isUserRetrieved()) {
                Toast.makeText(
                    context,
                    "Login Error",
                    Toast.LENGTH_SHORT).show()
                loginViewModel.loginFailed()
            }

        })
    }
}