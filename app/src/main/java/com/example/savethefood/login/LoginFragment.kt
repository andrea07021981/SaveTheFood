package com.example.savethefood.login

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.transition.TransitionInflater
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import br.com.simplepass.loadingbutton.animatedDrawables.ProgressType
import br.com.simplepass.loadingbutton.customViews.ProgressButton
import com.example.savethefood.BaseFragment
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentLoginBinding
import com.example.savethefood.shared.Greeting
import com.example.savethefood.shared.utils.EventObserver
import com.example.savethefood.shared.utils.LoginAuthenticationStates
import com.example.savethefood.shared.utils.LoginAuthenticationStates.*
import com.example.savethefood.shared.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    // TODO when koin added, use sharedViewModel
    override val viewModel: LoginViewModel by sharedViewModel()

    override val layoutRes: Int
        get() = R.layout.fragment_login

    override val classTag: String
        get() = LoginFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.custommove)
    }

    override fun init() {
        with(dataBinding) {
            loginViewModel = viewModel
        }
        Log.d(classTag, "Init done")
        Log.i("Login Activity", "Hello from shared module: " + (Greeting().greeting()))
    }


    override fun activateObservers() {
        viewModel.navigateToSignUp.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                findNavController()
                    .navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
            }
        })

        viewModel.loginAuthenticationState.observe(this.viewLifecycleOwner, Observer {
            if (it is Authenticated || it is Authenticating || it is InvalidAuthentication) {
                dataBinding.loginButton.run { morphDoneAndRevert(requireContext(), it) }
            } else if (it is Unauthenticated) {
                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.genericError.observe(viewLifecycleOwner) {
            // TODO temporary, need to add the error in textfield
            Toast.makeText(context, it.joinToString(separator = "\n"), Toast.LENGTH_SHORT).show()
        }

        // Example of observing a State, this ext is lifecycle aware
/*        viewModel.uiState.collectWhen(this) {

        }*/
    }

    private fun ProgressButton.morphDoneAndRevert(
        context: Context,
        state: LoginAuthenticationStates?,
        fillColor: Int = ContextCompat.getColor(context, R.color.customGreen),
        bitmap: Bitmap = defaultDoneImage(context.resources),
        doneTime: Long = 3000,
        navigateTime: Long = 1000,
        revertTime: Long = 4000
    ) {
        progressType = ProgressType.INDETERMINATE

        when (state) {
            is Authenticating -> {
                startAnimation()
            }
            is Authenticated -> {
                Handler().run {
                    doneLoadingAnimation(fillColor, bitmap)
                    postDelayed({
                        val bundle = bundleOf(
                            "x" to dataBinding.loginButton.x,
                            "y" to dataBinding.loginButton.y
                        )
                        findNavController()
                            .navigate(
                                LoginFragmentDirections.actionLoginFragmentToHomeFragmentContainer(
                                    bundle
                                )
                            )
                    }, navigateTime)
                    viewModel.resetState()
                }
            }
            is Unauthenticated -> {
                Handler().run {
                    postDelayed(::revertAnimation, revertTime)
                }
            }
            is InvalidAuthentication -> {
                val fillColorError = ContextCompat.getColor(context, R.color.customRed)
                val bitmapError: Bitmap = BitmapFactory.decodeResource(
                    resources,
                    R.mipmap.ic_check_ko
                )
                Handler().run {
                    postDelayed({ doneLoadingAnimation(fillColorError, bitmapError) }, doneTime)
                    postDelayed(::revertAnimation, revertTime)
                }
            }
            else -> Unit
        }

    }

    private fun defaultDoneImage(resources: Resources) =
        BitmapFactory.decodeResource(resources, R.mipmap.ic_check_ok)
}