package com.example.savethefood.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.savethefood.BaseFragment
import com.example.savethefood.EventObserver
import com.example.savethefood.R
import com.example.savethefood.data.source.repository.UserDataRepository
import com.example.savethefood.databinding.FragmentSignupBinding

class SignUpFragment : BaseFragment<SignUpViewModel, FragmentSignupBinding>() {

    //We can use by viewModels when the VM is not shared with other fragments
    override val viewModel by viewModels<SignUpViewModel>() {
            SignUpViewModel.SignUpViewModelFactory(UserDataRepository.getRepository(requireActivity().application))
        }

    override val layoutRes: Int
        get() = R.layout.fragment_signup

    override val classTag: String
        get() = SignUpFragment::class.java.simpleName

    override fun init() {
        dataBinding.also {
            it.signupViewModel = viewModel
        }
    }

    override fun activateObservers() {
        viewModel.loginEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                this
                    .findNavController()
                    .popBackStack()
            }
        })
    }
}