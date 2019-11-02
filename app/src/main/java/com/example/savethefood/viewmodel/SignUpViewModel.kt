package com.example.savethefood.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.savethefood.local.domain.User

class SignUpViewModel : BaseObservable() {

    //TODO start from here, change BaseObservable to viewmodel

    //https://www.journaldev.com/22561/android-mvvm-livedata-data-binding
    var user = User()

    @Bindable
    fun getUsername(): String {
        return user.username
    }

    fun setUsername(value: String) {
        // Avoids infinite loops.
        if (user.username != value) {
            user.username = value

            // Notify observers of a new value.
            //notifyPropertyChanged(BR.)
        }
    }

    init {

    }

    fun checkInfo(){

    }
}