package com.example.savethefood.shared.viewmodel

import com.example.savethefood.shared.data.Result
import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.data.source.repository.UserRepository
import com.example.savethefood.shared.utils.Event
import com.example.savethefood.shared.utils.LoginAuthenticationStates
import com.example.savethefood.shared.utils.LoginStateValue
import com.example.savethefood.shared.utils.isValidEmail
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userDataRepository: UserRepository
) : ViewModel() {

    companion object {
        private val TAG = LoginViewModel::class.simpleName
    }

    private val _genericError = MutableLiveData<Event<Unit>>(Event(Unit))
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

    public val email = object : LoginStatus("") {
        init {

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

        }

        override val checkStatus: (String) -> LoginStateValue = {
            when {
                !it.isValidEmail() -> LoginStateValue.INVALID_FORMAT
                it.isEmpty() -> LoginStateValue.INVALID_LENGTH
                else -> LoginStateValue.NONE
            }
        }
    }

    private val _signUpEvent = MutableLiveData<Event<Long>>(Event(0L))
    val signUpEvent: LiveData<Event<Long>>
        get() = _signUpEvent

    private val _loginAuthenticationState = MutableLiveData<LoginAuthenticationStates>(LoginAuthenticationStates.Idle)
    val loginAuthenticationState: LiveData<LoginAuthenticationStates>
        get() = _loginAuthenticationState

    private val _navigateToSignUpFragment = MutableLiveData<Event<Unit>>(Event(Unit))
    val navigateToSignUpFragment: LiveData<Event<Unit>>
        get() = _navigateToSignUpFragment

    fun onSignInClick(){
        when {
            email.valueStatus.value == LoginStateValue.EMPTY_FIELD ||
                    password.valueStatus.value == LoginStateValue.EMPTY_FIELD -> _genericError.value = Event(Unit)
            email.valueStatus.value == LoginStateValue.NONE &&
                    password.valueStatus.value == LoginStateValue.NONE ->
                doLogin {
                    userDataRepository.getUser(UserDomain(email = email.value, password = password.value))
                }
        }
    }

    private inline fun doLogin(crossinline block: suspend () -> Result<UserDomain>) {
        viewModelScope.launch {
            _loginAuthenticationState.value = LoginAuthenticationStates.Authenticating()
            when (val result = block()) {
                is Result.Success -> {
                    _loginAuthenticationState.value =
                        LoginAuthenticationStates.Authenticated(user = result.data)
                }
                is Result.Error -> _loginAuthenticationState.value =
                    LoginAuthenticationStates.InvalidAuthentication(result.message)
                is Result.ExError -> _loginAuthenticationState.value =
                    LoginAuthenticationStates.InvalidAuthentication(result.exception.toString())
                is Result.Loading -> _loginAuthenticationState.value =
                    LoginAuthenticationStates.Authenticating()
                else -> LoginAuthenticationStates.Idle
            }
        }
    }

    fun onSignUpClick(){
        when {
            userName.valueStatus.value == LoginStateValue.EMPTY_FIELD ||
                    email.valueStatus.value == LoginStateValue.EMPTY_FIELD ||
                    password.valueStatus.value == LoginStateValue.EMPTY_FIELD -> _genericError.value = Event(Unit)
            email.valueStatus.value == LoginStateValue.NONE &&
                    email.valueStatus.value == LoginStateValue.NONE &&
                    password.valueStatus.value.equals(LoginStateValue.NONE) ->
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