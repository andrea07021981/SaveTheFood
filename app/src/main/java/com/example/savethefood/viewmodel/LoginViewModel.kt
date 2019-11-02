package com.example.savethefood.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.savethefood.R
import com.example.savethefood.constants.LoginAuthenticationStates
import com.example.savethefood.constants.UNAUTHENTICATED

class LoginViewModel : ViewModel() {

    val animationResourceView = R.anim.fade_in
    val animationResourceButton = R.anim.bounce

    private val _autenticationState = MutableLiveData<LoginAuthenticationStates>(
        UNAUTHENTICATED
    )
    val autenticationState: LiveData<LoginAuthenticationStates>
        get() = _autenticationState

    private val _navigateToHomeFragment = MutableLiveData<Boolean>()
    val navigateToHomeFragment: LiveData<Boolean>
        get() = _navigateToHomeFragment

    private val _navigateToSignUpFragment = MutableLiveData<Boolean>()
    val navigateToSignUpFragment: LiveData<Boolean>
        get() = _navigateToSignUpFragment

    init {

    }

    fun doneNavigationHome() {
        _navigateToHomeFragment.value = null
    }

    fun doneNavigationSignUp() {
        _navigateToSignUpFragment.value = null
    }

    fun moveToHome() {
        _navigateToHomeFragment.value = true
    }

    fun moveToSignUp() {
        _navigateToSignUpFragment.value = true
    }
}