package com.example.savethefood.login

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.BuildConfig
import com.example.savethefood.Event
import com.example.savethefood.R
import com.example.savethefood.constants.LoginAuthenticationStates
import com.example.savethefood.constants.LoginAuthenticationStates.*
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class LoginViewModel @ViewModelInject constructor(
    private val userDataRepository: UserRepository
) : ViewModel() {

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }

    val animationResourceView = R.anim.fade_in
    val animationResourceButton = R.anim.bounce

    val emailValue = MutableLiveData<String>()

    var passwordValue = MutableLiveData<String>()

    var errorEmail = Transformations.map(emailValue) {
        it.isNullOrBlank()
    }
    var errorPassword = Transformations.map(passwordValue) {
        it.isNullOrBlank()
    }

    private val _loginAuthenticationState = MutableLiveData<LoginAuthenticationStates>()
    val loginAuthenticationState: LiveData<LoginAuthenticationStates>
        get() = _loginAuthenticationState

    private val _navigateToSignUpFragment = MutableLiveData<Event<Unit>>()
    val navigateToSignUpFragment: LiveData<Event<Unit>>
        get() = _navigateToSignUpFragment

    init {
        if (BuildConfig.DEBUG) {
            emailValue.value = "a@a.com"
            passwordValue.value = "a"
        }
    }

    fun onSignUpClick(){
        if (errorEmail.value == false && errorPassword.value == false) {
            doLogin {
                userDataRepository.getUser(user = UserDomain().apply {
                    userEmail = emailValue.value.toString()
                    userPassword = passwordValue.value.toString()
                })
            }
        }
    }

    private inline fun doLogin(crossinline block: suspend () -> Result<UserDomain>) {
        viewModelScope.launch {
            _loginAuthenticationState.value = Authenticating()
            when (val result = block()) {
                is Result.Success -> _loginAuthenticationState.value = Authenticated(user = result.data)
                is Result.Error -> _loginAuthenticationState.value = InvalidAuthentication(result.message)
                is Result.ExError -> _loginAuthenticationState.value = InvalidAuthentication(result.exception.toString())
                is Result.Loading -> _loginAuthenticationState.value = Authenticating()
                else -> Idle
            }
        }
    }

    fun resetState() {
        _loginAuthenticationState.value = Idle
    }

    fun moveToSignUp() {
        _navigateToSignUpFragment.value = Event(Unit)
    }
}
