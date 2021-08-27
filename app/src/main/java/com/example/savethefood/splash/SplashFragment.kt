package com.example.savethefood.splash

import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.savethefood.BaseFragment
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentSplashBinding
import com.example.savethefood.shared.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>() {

    override val viewModel: SplashViewModel by viewModel()

    override val layoutRes: Int
        get() = R.layout.fragment_splash

    override val classTag: String
        get() = SplashFragment::class.java.simpleName

    override fun init() {
        with(dataBinding) {
            splashViewModel = viewModel
        }
    }
    override fun activateObservers() {
        viewModel.loginEvent.observe(this.viewLifecycleOwner, {
            it.let {
                val extras = FragmentNavigatorExtras(
                    dataBinding.chefImageview to "chef_imageview"
                )
                this
                    .findNavController()
                    .navigate(R.id.action_splashFragment_to_loginFragment,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.splashFragment,
                                true).build(),
                        extras)
            }
        })
    }
}