package com.example.savethefood.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    val _autenticationState = MutableLiveData<LoginAuthenticationStates>(UNAUTHENTICATED)

    init {
        
    }
}