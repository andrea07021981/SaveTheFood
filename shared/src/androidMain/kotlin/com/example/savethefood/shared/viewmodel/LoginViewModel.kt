package com.example.savethefood.shared.viewmodel

import androidx.lifecycle.*
import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.data.source.repository.UserRepository
import com.example.savethefood.shared.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginState(
    val userName: LoginViewModel.LoginStatus,
    val email: LoginViewModel.LoginStatus,
    val password: LoginViewModel.LoginStatus,
    val authState: LoginAuthenticationStates
) {

}
// TODO move livedata to MutableStateFlow like jetenews and private set
actual class LoginViewModel actual constructor(
    val userDataRepository: UserRepository
) : ViewModel() {

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }

    private val _genericError = MutableLiveData<ArrayList<String>>()
    val genericError: LiveData<ArrayList<String>> = _genericError

    abstract class LoginStatus(var value: String) {

        private var _valueStatus = MutableLiveData(LoginStateValue.EMPTY_FIELD)
        val valueStatus: LiveData<LoginStateValue> = _valueStatus

        internal var errMessage = LoginStateValue.EMPTY_FIELD.message

        open val onFocusChanged: (String) -> Unit = {
            with(checkStatus(it)) {
                _valueStatus.value = this
                errMessage = message
            }
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
        override val checkStatus: (String) -> LoginStateValue = {
            when {
                !it.isValidEmail() -> LoginStateValue.INVALID_FORMAT
                it.isEmpty() -> LoginStateValue.INVALID_LENGTH
                else -> LoginStateValue.NONE
            }
        }

        init {
            // TODO Dev phase
            value = "a@a.com"
            with(checkStatus(value)) {
                errMessage = message
            }
        }
    }

    val password = object : LoginStatus("") {
        override val checkStatus: (String) -> LoginStateValue = {
            when {
                !it.isValidPassword() -> LoginStateValue.INVALID_FORMAT
                it.isEmpty() -> LoginStateValue.INVALID_LENGTH
                else -> LoginStateValue.NONE
            }
        }

        init {
            value = "aaaaaaaa"
            with(checkStatus(value)) {
                errMessage = message
            }
        }
    }

    // TODO prepared like jetnews for the next step livedata -> StateFLow (use _uiState.update { it.copy(authState....)
    // TODO replace XML with uistate
    var _uiState = MutableStateFlow(
        LoginState(
            userName,
            email,
            password,
            LoginAuthenticationStates.Idle
        )
    )
    private set
    val uiState: StateFlow<LoginState> = _uiState

    private val _signUpEvent = MutableLiveData<Event<Long>>()
    val signUpEvent: LiveData<Event<Long>>
        get() = _signUpEvent

    private val _loginAuthenticationState = MutableLiveData<LoginAuthenticationStates>()
    val loginAuthenticationState: LiveData<LoginAuthenticationStates>
        get() = _loginAuthenticationState

    private val _navigateToSignUp = MutableLiveData<Event<Unit>>()
    val navigateToSignUp: LiveData<Event<Unit>>
        get() = _navigateToSignUp

    fun onSignInClick() {
        val errorMessages = checkErrors()
        if (errorMessages.isEmpty()) {
            doLogin {
                userDataRepository.getUser(
                    UserDomain(
                        userName = email.value,
                        email = email.value,
                        password = password.value
                    )
                )
            }
        } else {
            _genericError.value = errorMessages
        }
    }

    private inline fun doLogin(crossinline block: suspend () -> com.example.savethefood.shared.data.Result<UserDomain>) {
        viewModelScope.launch {
            _loginAuthenticationState.value = LoginAuthenticationStates.Authenticating()
            _uiState.update { it.copy(authState = LoginAuthenticationStates.Authenticating()) }
            when (val result = block()) {
                is com.example.savethefood.shared.data.Result.Success -> {
                    _loginAuthenticationState.value =
                        LoginAuthenticationStates.Authenticated(user = result.data)
                    _uiState.update { it.copy(authState = LoginAuthenticationStates.Authenticated(user = result.data)) }
                }
                is com.example.savethefood.shared.data.Result.Error -> {
                    _loginAuthenticationState.value =
                        LoginAuthenticationStates.InvalidAuthentication(result.message)
                    _uiState.update { it.copy(authState = LoginAuthenticationStates.InvalidAuthentication(result.message)) }
                }
                is com.example.savethefood.shared.data.Result.ExError -> {
                    _loginAuthenticationState.value =
                        LoginAuthenticationStates.InvalidAuthentication(result.exception.toString())
                    _uiState.update { it.copy(authState = LoginAuthenticationStates.InvalidAuthentication("User not found")) }
                }
                is com.example.savethefood.shared.data.Result.Loading -> {
                    _loginAuthenticationState.value =
                        LoginAuthenticationStates.Authenticating()
                    _uiState.update { it.copy(authState = LoginAuthenticationStates.Authenticating()) }
                }
                else -> {
                    LoginAuthenticationStates.Idle
                    _uiState.update { it.copy(authState = LoginAuthenticationStates.Idle) }
                }
            }
        }
    }

    fun onSignUpClick() {
        val errorMessages = checkErrors(true)
        if (errorMessages.isEmpty()) {
            viewModelScope.launch {
                val newUserId = userDataRepository.saveNewUser(
                    UserDomain(
                        userName = userName.value,
                        email = email.value,
                        password = password.value
                    )
                )
                _signUpEvent.value = Event(newUserId)
            }
        } else {
            _genericError.value = errorMessages
        }
    }

    private fun checkErrors(isSignUp: Boolean = false): ArrayList<String> {
        val errorMessages = arrayListOf<String>()
        if (userName.errMessage.isNotEmpty() && isSignUp) {
            errorMessages.add("Username ${userName.errMessage}")
        }
        if (email.errMessage.isNotEmpty()) {
            errorMessages.add("Email ${email.errMessage}")
        }
        if (password.errMessage.isNotEmpty()) {
            errorMessages.add("Password ${password.errMessage}")
        }
        return errorMessages
    }

    fun resetState() {
        _loginAuthenticationState.value = LoginAuthenticationStates.Idle
        _uiState.update { it.copy(authState = LoginAuthenticationStates.Idle) }
    }

    fun moveToSignUp() {
        _navigateToSignUp.value = Event(Unit)
    }
}
