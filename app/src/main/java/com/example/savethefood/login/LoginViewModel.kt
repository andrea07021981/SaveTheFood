package com.example.savethefood.login

import android.os.Build
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.BuildConfig
import com.example.savethefood.Event
import com.example.savethefood.R
import com.example.savethefood.constants.*
import com.example.savethefood.constants.LoginAuthenticationStates.*
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.repository.UserDataRepository
import com.example.savethefood.data.source.repository.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LoginViewModel (
    private val userDataRepository: UserRepository
) : ViewModel() {

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }

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
        errorEmail.value = emailValue.value.isNullOrEmpty()
        errorPassword.value = passwordValue.value.isNullOrEmpty()
        if (errorEmail.value == false && errorPassword.value == false) {
            //TODO add a generic base viewmodel class with a generic function loadData(block: suspend () -> Unit)
            doLogin {
                userDataRepository.getUser(user = UserDomain().apply {
                    userEmail = emailValue.value.toString()
                    userPassword = passwordValue.value.toString()
                })
            }
        }
    }

    // TODO remove flow, use call direct and repository send the different states _loginAuthenticationState.value = userDataRepository.getUse... Move login auth state in repo and all this code
    private fun doLogin(block: suspend () -> Result<UserDomain>) {
        viewModelScope.launch {
            _loginAuthenticationState.value = Authenticating()
            val result = block()
            when (result) {
                    is Result.Success -> _loginAuthenticationState.value = Authenticated(user = result.data)
                    is Result.Error -> _loginAuthenticationState.value = InvalidAuthentication(result.message)
                    is Result.ExError -> _loginAuthenticationState.value = InvalidAuthentication(result.exception.toString())
                    is Result.Loading -> _loginAuthenticationState.value = Authenticating()
                }
        }
    }

    fun resetState() {
        _loginAuthenticationState.value = Idle
    }

    fun moveToSignUp() {
        _navigateToSignUpFragment.value = Event(Unit)
    }

    /**
     * Factory for constructing LoginViewModel with parameter
     */
    @Deprecated("Added hilt DI")
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