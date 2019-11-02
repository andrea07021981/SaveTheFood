package com.example.savethefood.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.savethefood.R

class SplashViewModel : ViewModel() {

    val animationResource = R.anim.shake_anim

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    init {
        //TODO load all stuff and update variable which move to next fragment in nav
        val timer = object: CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                _navigateToLogin.value = true
            }
        }
        timer.start()
    }

    fun doneNavigating() {
        _navigateToLogin.value = null
    }
}