package com.example.savethefood.login

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.savethefood.BaseFragment
import com.example.savethefood.EventObserver
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

//TODO Add ontext change to the strenght password and use library https://github.com/nulab/zxcvbn4j/issues/75 and binding, customview like
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SignUpFragment : BaseFragment<LoginViewModel, FragmentSignupBinding>() {

    //We can use by viewModels when the VM is not shared with other fragments
    override val viewModel by activityViewModels<LoginViewModel>()

    override val layoutRes: Int
        get() = R.layout.fragment_signup

    override val classTag: String
        get() = SignUpFragment::class.java.simpleName

    override fun init() {
        with(dataBinding) {
            loginViewModel = viewModel
        }
    }

    override fun activateObservers() {
        viewModel.signUpEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                this
                    .findNavController()
                    .popBackStack()
            }
        })
    }
}