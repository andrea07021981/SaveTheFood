package com.example.savethefood.splash

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.savethefood.Event
import com.example.savethefood.R

class SplashViewModel : ViewModel() {

    val animationResource = R.anim.bounce

    private val _loginEvent = MutableLiveData<Event<Unit>>()
    val loginEvent: LiveData<Event<Unit>>
        get() = _loginEvent

    init {
        val timer = object: CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                _loginEvent.value = Event(Unit)
            }
        }
        timer.start()
    }
}