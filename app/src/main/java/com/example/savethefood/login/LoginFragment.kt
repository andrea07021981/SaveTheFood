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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.simplepass.loadingbutton.animatedDrawables.ProgressType
import br.com.simplepass.loadingbutton.customViews.ProgressButton
import com.example.savethefood.BaseFragment
import com.example.savethefood.EventObserver
import com.example.savethefood.R
import com.example.savethefood.constants.*
import com.example.savethefood.constants.LoginAuthenticationStates.*
import com.example.savethefood.data.source.repository.UserDataRepository
import com.example.savethefood.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override val viewModel by viewModels<LoginViewModel> {
        LoginViewModel.LoginViewModelFactory(UserDataRepository.getRepository(requireActivity().application))
    }

    override val layoutRes: Int
        get() = R.layout.fragment_login

    override val classTag: String
        get() = LoginFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.custommove)
    }

    override fun init() {
        dataBinding.also {
            it.loginViewModel = viewModel
        }
        Log.d(classTag, "Init done")
    }


    override fun activateObservers() {
        viewModel.navigateToSignUpFragment.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                findNavController()
                    .navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
            }
        })

        viewModel.loginAuthenticationState.observe(this.viewLifecycleOwner, Observer {
            if (it is Authenticated || it is Authenticating || it is InvalidAuthentication) {
                dataBinding.loginButton.run { morphDoneAndRevert(requireNotNull(activity), it) }
            } else if (it is Unauthenticated) {
                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
            }
        })
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
                        bundle.putParcelable("user", state.user)
                        findNavController()
                            .navigate(
                                LoginFragmentDirections.actionLoginFragmentToHomeFragment(
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
        }

    }

    private fun defaultDoneImage(resources: Resources) =
        BitmapFactory.decodeResource(resources, R.mipmap.ic_check_ok)
}