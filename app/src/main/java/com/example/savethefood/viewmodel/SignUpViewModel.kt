package com.example.savethefood.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.savethefood.local.domain.User
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Patterns


class SignUpViewModel : ViewModel() {

    var user = User()
    var userNameValue = MutableLiveData<String>()
    var errorUserName = MutableLiveData<Boolean>()
    var emailValue = MutableLiveData<String>()
    var errorEmail = MutableLiveData<Boolean>()
    var passwordValue = MutableLiveData<String>()
    var errorPassword = MutableLiveData<Boolean>()

    init {
        userNameValue.value = ""
        emailValue.value = ""
        passwordValue.value = ""
    }

    fun onSignUpClick(){
        if (!checkValues()) {
            user.apply {
                userName = userNameValue.value.toString()
                userEmail = emailValue.value.toString()
                userPassword = passwordValue.value.toString()
            }
            //TODO save
        }
    }

    fun checkValues (): Boolean {
        errorUserName.value = userNameValue.value.isNullOrEmpty()
        errorEmail.value = !Patterns.EMAIL_ADDRESS.matcher(emailValue.value).matches()
        errorPassword.value = passwordValue.value.isNullOrEmpty()
        return errorUserName.value!! && errorEmail.value!! && errorPassword.value!!
    }
}