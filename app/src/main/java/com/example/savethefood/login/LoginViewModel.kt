package com.example.savethefood.login

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.example.savethefood.R
import com.example.savethefood.constants.Authenticated
import com.example.savethefood.constants.Authenticating
import com.example.savethefood.constants.InvalidAuthentication
import com.example.savethefood.constants.LoginAuthenticationStates
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.repository.UserRepository
import kotlinx.coroutines.*

class LoginViewModel(
    private val userDataRepository: UserRepository
) : ViewModel() {

    val animationResourceView = R.anim.fade_in
    val animationResourceButton = R.anim.bounce

    var emailValue = MutableLiveData<String>()
        @VisibleForTesting set // this allow us to use this set only for test
    var passwordValue = MutableLiveData<String>()
        @VisibleForTesting set // this allow us to use this set only for test
    var errorPassword = MutableLiveData<Boolean>()
    var errorEmail = MutableLiveData<Boolean>()

    private val _loginAuthenticationState = MutableLiveData<LoginAuthenticationStates>()
    val loginAuthenticationState: LiveData<LoginAuthenticationStates>
        get() = _loginAuthenticationState

    private val _navigateToSignUpFragment = MutableLiveData<Boolean>()
    val navigateToSignUpFragment: LiveData<Boolean>
        get() = _navigateToSignUpFragment

    init {
        emailValue.value = "a@a.com"
        passwordValue.value = "a"
    }

    fun onSignUpClick(){
        errorEmail.value = emailValue.value.isNullOrEmpty()
        errorPassword.value = passwordValue.value.isNullOrEmpty()
        if (errorEmail.value == false && errorPassword.value == false) {
            doLogin()
        }
    }

    private fun doLogin() {

        viewModelScope.launch {
            var result: Result<UserDomain>? = null
            _loginAuthenticationState.value = Authenticating()
            result = userDataRepository.getUser(user = UserDomain()
                .apply {
                    userEmail = emailValue.value.toString()
                    userPassword = passwordValue.value.toString()
                })
            when (result) {
                is Result.Success -> _loginAuthenticationState.value = Authenticated(user = result.data)
                is Result.Error -> _loginAuthenticationState.value = InvalidAuthentication(result.message)
                else -> _loginAuthenticationState.value = null
            }
        }
    }

    fun doneNavigationSignUp() {
        _navigateToSignUpFragment.value = null
    }

    fun moveToSignUp() {
        _navigateToSignUpFragment.value = true
    }

    fun resetState() {
        _loginAuthenticationState.value = null
    }
    /**
     * Factory for constructing LoginViewModel with parameter
     */
    class LoginViewModelFactory(
        private val dataRepository: UserRepository
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(dataRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}