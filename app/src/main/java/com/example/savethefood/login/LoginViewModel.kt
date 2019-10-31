package com.example.savethefood.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.savethefood.R

class LoginViewModel : ViewModel() {

    val animationResourceView = R.anim.fade_in
    val animationResourceButton = R.anim.bounce

    private val _autenticationState = MutableLiveData<LoginAuthenticationStates>(UNAUTHENTICATED)
    val autenticationState: LiveData<LoginAuthenticationStates>
        get() = _autenticationState

    init {

    }
}