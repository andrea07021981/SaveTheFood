package com.example.savethefood.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.BuildConfig
import com.example.savethefood.Event
import com.example.savethefood.R
import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.data.source.repository.UserRepository
import com.example.savethefood.shared.utils.LoginAuthenticationStates
import com.example.savethefood.shared.utils.LoginStateValue
import com.example.savethefood.util.isValidEmail
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


    private val _genericError = MutableLiveData<Event<Unit>>()
    val genericError: LiveData<Event<Unit>> = _genericError

    abstract class LoginStatus(var value: String) {
        private var _valueStatus = MutableLiveData<LoginStateValue>(LoginStateValue.NONE)
        val valueStatus: LiveData<LoginStateValue> = _valueStatus

        open val onFocusChanged: (String) -> Unit = {
            _valueStatus.value = checkStatus(it)
        }

        abstract val checkStatus: (String) -> LoginStateValue
    }

    val userName = object : LoginStatus("") {
        override val checkStatus: (String) -> LoginStateValue = {
            when {
                it.isEmpty() -> LoginStateValue.INVALID_LENGTH
                else -> LoginStateValue.NONE
            }
        }
    }

    val email = object : LoginStatus("") {
        init {
            if (BuildConfig.DEBUG) {
                value = "a@a.com"
            }
        }

        override val checkStatus: (String) -> LoginStateValue = {
            when {
                !it.isValidEmail() -> LoginStateValue.INVALID_FORMAT
                it.isEmpty() -> LoginStateValue.INVALID_LENGTH
                else -> LoginStateValue.NONE
            }
        }
    }

    val password = object : LoginStatus("") {
        init {
            if (BuildConfig.DEBUG) {
                value = "aaaaaaaa"
            }
        }

        override val checkStatus: (String) -> LoginStateValue = {
            when {
                !it.isValidEmail() -> LoginStateValue.INVALID_FORMAT
                it.isEmpty() -> LoginStateValue.INVALID_LENGTH
                else -> LoginStateValue.NONE
            }
        }
    }

    private val _signUpEvent = MutableLiveData<Event<Long>>()
    val signUpEvent: LiveData<Event<Long>>
        get() = _signUpEvent

    private val _loginAuthenticationState = MutableLiveData<LoginAuthenticationStates>()
    val loginAuthenticationState: LiveData<LoginAuthenticationStates>
        get() = _loginAuthenticationState

    private val _navigateToSignUpFragment = MutableLiveData<Event<Unit>>()
    val navigateToSignUpFragment: LiveData<Event<Unit>>
        get() = _navigateToSignUpFragment

    fun onSignInClick(){
        when {
            email.valueStatus.value?.equals(LoginStateValue.EMPTY_FIELD) == true ||
                    password.valueStatus.value?.equals(LoginStateValue.EMPTY_FIELD) == true  -> _genericError.value = Event(Unit)
            email.valueStatus.value?.equals(LoginStateValue.NONE) == true &&
                    password.valueStatus.value?.equals(LoginStateValue.NONE) == true ->
                doLogin {
                    userDataRepository.getUser(
                        UserDomain(
                        userName = email.value,
                        email = email.value,
                        password = password.value)
                    )
                }
        }
    }

    private inline fun doLogin(crossinline block: suspend () -> com.example.savethefood.shared.data.Result<UserDomain>) {
        viewModelScope.launch {
            _loginAuthenticationState.value = LoginAuthenticationStates.Authenticating()
            when (val result = block()) {
                is com.example.savethefood.shared.data.Result.Success -> {
                    _loginAuthenticationState.value =
                        LoginAuthenticationStates.Authenticated(user = result.data)
                }
                is com.example.savethefood.shared.data.Result.Error -> _loginAuthenticationState.value =
                    LoginAuthenticationStates.InvalidAuthentication(result.message)
                is com.example.savethefood.shared.data.Result.ExError -> _loginAuthenticationState.value =
                    LoginAuthenticationStates.InvalidAuthentication(result.exception.toString())
                is com.example.savethefood.shared.data.Result.Loading -> _loginAuthenticationState.value =
                    LoginAuthenticationStates.Authenticating()
                else -> LoginAuthenticationStates.Idle
            }
        }
    }

    fun onSignUpClick(){
        when {
            userName.valueStatus.value?.equals(LoginStateValue.EMPTY_FIELD) == true ||
                email.valueStatus.value?.equals(LoginStateValue.EMPTY_FIELD) == true ||
                    password.valueStatus.value?.equals(LoginStateValue.EMPTY_FIELD) == true  -> _genericError.value = Event(Unit)
            email.valueStatus.value?.equals(LoginStateValue.NONE) == true &&
                email.valueStatus.value?.equals(LoginStateValue.NONE) == true &&
                    password.valueStatus.value?.equals(LoginStateValue.NONE) == true ->
                viewModelScope.launch {
                    val newUserId = userDataRepository.saveNewUser(UserDomain(
                        userName = userName.value,
                        email = email.value,
                        password = password.value))
                    _signUpEvent.value = Event(newUserId)
                }
        }
    }

    fun resetState() {
        _loginAuthenticationState.value = LoginAuthenticationStates.Idle
    }

    fun moveToSignUp() {
        _navigateToSignUpFragment.value = Event(Unit)
    }
}
