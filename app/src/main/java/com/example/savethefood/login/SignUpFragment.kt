package com.example.savethefood.login

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.savethefood.BaseFragment
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentSignupBinding
import com.example.savethefood.shared.utils.EventObserver
import com.example.savethefood.shared.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

//TODO Add ontext change to the strenght password and use library https://github.com/nulab/zxcvbn4j/issues/75 and binding
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SignUpFragment : BaseFragment<LoginViewModel, FragmentSignupBinding>() {

    //We can use by viewModels when the VM is not shared with other fragments
    override val viewModel: LoginViewModel by sharedViewModel()

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

        viewModel.genericError.observe(viewLifecycleOwner) {
            // TODO temporary, need to add the error in textfield
            Toast.makeText(context, it.joinToString(separator = "\n"), Toast.LENGTH_SHORT).show()
        }
    }
}