package com.example.savethefood.viewmodel

import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.savethefood.local.domain.User

class SignUpViewModel : BaseObservable() {

    //TODO start from here, change user class with properties and no costructor and use databingin
    //https://kotlinlang.org/docs/reference/properties.html
    val user = User()



    init {

    }

    fun checkInfo(){

    }
}