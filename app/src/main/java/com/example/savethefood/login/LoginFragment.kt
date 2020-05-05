package com.example.savethefood.login

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
import androidx.navigation.fragment.findNavController
import com.example.savethefood.R
import com.example.savethefood.data.Result
import com.example.savethefood.data.source.repository.UserDataRepository
import com.example.savethefood.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    companion object {
        private val TAG = LoginFragment::class.java.name
    }

    private val loginViewModel by viewModels<LoginViewModel> {
        LoginViewModel.LoginViewModelFactory(UserDataRepository.getRepository(requireActivity().application))
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
                if (it is Result.Success) {
                    Log.d(TAG, "User logged with ${it.data.userName} and ${it.data.userPassword} ")
                    this
                        .findNavController()
                        .navigate(
                            LoginFragmentDirections.actionLoginFragmentToNestedNavGraph(
                                it.data
                            )
                        )
                    loginViewModel.doneNavigationHome()
                } else if (it is Result.Error) {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}