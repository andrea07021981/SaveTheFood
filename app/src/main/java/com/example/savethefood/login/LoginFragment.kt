package com.example.savethefood.login

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.simplepass.loadingbutton.animatedDrawables.ProgressType
import br.com.simplepass.loadingbutton.customViews.ProgressButton
import com.example.savethefood.R
import com.example.savethefood.constants.*
import com.example.savethefood.data.Result
import com.example.savethefood.data.source.repository.UserDataRepository
import com.example.savethefood.databinding.FragmentLoginBinding
import com.example.savethefood.util.morphDoneAndRevert
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
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
        //TODO fix the error after signup and come back
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

        loginViewModel.loginAuthenticationState.observe(this.viewLifecycleOwner, Observer {
            if (it is Authenticated || it is Authenticating || it is InvalidAuthentication) {
                dataBinding.loginButton.run {
                    if (morphDoneAndRevert(requireNotNull(activity), it, resources = resources)) {
                        val bundle = bundleOf("x" to dataBinding.loginButton.x, "y" to dataBinding.loginButton.y)
                        bundle.putParcelable("user", (it as Authenticated).user)
                        findNavController()
                            .navigate(
                                LoginFragmentDirections.actionLoginFragmentToHomeFragment(
                                    bundle
                                )
                            )
                        loginViewModel.resetState()
                    }
                }
            } else if (it is Unauthenticated){
                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

}