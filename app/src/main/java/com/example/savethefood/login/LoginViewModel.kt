package com.example.savethefood.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _autenticationState = MutableLiveData<LoginAuthenticationStates>(UNAUTHENTICATED)
    val autenticationState: LiveData<LoginAuthenticationStates>
        get() = _autenticationState

    init {

    }
}