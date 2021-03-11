package com.example.savethefood.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.BuildConfig
import com.example.savethefood.Event
import com.example.savethefood.R
import com.example.savethefood.constants.LoginAuthenticationStates
import com.example.savethefood.constants.LoginAuthenticationStates.*
import com.example.savethefood.constants.LoginError
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.repository.UserRepository
import com.example.savethefood.util.isValidEmail
import com.example.savethefood.util.isValidPassword
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private val _errorEmail = MutableLiveData<LoginError>(LoginError.NONE)
    val errorEmail: LiveData<LoginError> = _errorEmail

    private val _errorPassword = MutableLiveData<LoginError>(LoginError.NONE)
    val errorPassword: LiveData<LoginError> = _errorPassword

    val onUserEmailFocusChanged: (String) -> Unit = {
        _errorEmail.value = when {
            it.isEmpty() || !it.isValidEmail() -> LoginError.INVALID_EMAIL
            else -> LoginError.NONE
        }
    }

    val onUserPasswordFocusChanged: (String) -> Unit = {
        _errorPassword.value = when {
            !it.isValidPassword() -> LoginError.INVALID_PASSWORD
            else -> LoginError.NONE
        }
    }

    var userDomain = MutableLiveData(UserDomain())

    private val _loginAuthenticationState = MutableLiveData<LoginAuthenticationStates>()
    val loginAuthenticationState: LiveData<LoginAuthenticationStates>
        get() = _loginAuthenticationState

    private val _navigateToSignUpFragment = MutableLiveData<Event<Unit>>()
    val navigateToSignUpFragment: LiveData<Event<Unit>>
        get() = _navigateToSignUpFragment

    init {
        if (BuildConfig.DEBUG) {
            userDomain.value?.let {
                it.email = "a@a.com"
                it.password = "a"
            }
        }
    }

    fun onSignUpClick(){
        if (_errorEmail.value?.equals(LoginError.NONE) == true &&
                _errorPassword.value?.equals(LoginError.NONE) == true) {
            doLogin {
                userDataRepository.getUser(userDomain.value!!)
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
