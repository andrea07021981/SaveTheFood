package com.example.savethefood.shared.viewmodel

import androidx.lifecycle.*
import com.example.savethefood.shared.data.Result
import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.data.source.repository.UserRepository
import com.example.savethefood.shared.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * TODO moving to stateflow we will need to collect in fragments as shown here https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda
 * until we change the UI to compose
 */
data class LoginState(
    var userName: LoginViewModel.LoginStatus,
    var email: LoginViewModel.LoginStatus,
    var password: LoginViewModel.LoginStatus,
    var authState: LoginAuthenticationStates
) {

}
// TODO move livedata to MutableStateFlow like jetenews and private set
// TODO use stateIn to expose the flows directly such as the food list in home
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
    // TODO replace XML with uistate. We do not need the backing field, use private set
    var uiState = MutableStateFlow(
        LoginState(
            userName,
            email,
            password,
            LoginAuthenticationStates.Idle
        )
    )
    private set

    // TODO All the Single Events can be replaced with MutableShareFlow (Not state flow), since it has the replay default value 0
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
                        userName = userName.value,
                        email = email.value,
                        password = password.value
                    )
                )
            }
        } else {
            _genericError.value = errorMessages
        }
    }

    private inline fun doLogin(
        scope: CoroutineScope = viewModelScope,
        crossinline block: suspend () -> Result<UserDomain>
    ) {
        scope.launch {
            _loginAuthenticationState.value = LoginAuthenticationStates.Authenticating()
            uiState.update { it.copy(authState = LoginAuthenticationStates.Authenticating()) }
            when (val result = block()) {
                is Result.Success -> {
                    _loginAuthenticationState.value =
                        LoginAuthenticationStates.Authenticated(user = result.data)
                    uiState.update { it.copy(authState = LoginAuthenticationStates.Authenticated(user = result.data)) }
                }
                is Result.Error -> {
                    _loginAuthenticationState.value =
                        LoginAuthenticationStates.InvalidAuthentication(result.message)
                    uiState.update { it.copy(authState = LoginAuthenticationStates.InvalidAuthentication(result.message)) }
                }
                is Result.ExError -> {
                    _loginAuthenticationState.value =
                        LoginAuthenticationStates.InvalidAuthentication(result.exception.toString())
                    uiState.update { it.copy(authState = LoginAuthenticationStates.InvalidAuthentication("User not found")) }
                }
                is Result.Loading -> {
                    _loginAuthenticationState.value =
                        LoginAuthenticationStates.Authenticating()
                    uiState.update { it.copy(authState = LoginAuthenticationStates.Authenticating()) }
                }
                else -> {
                    LoginAuthenticationStates.Idle
                    uiState.update { it.copy(authState = LoginAuthenticationStates.Idle) }
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

    /**
     * Sign up with the Auth State
     */
    fun onSignUpClickState() {
        uiState.update { it.copy(authState = LoginAuthenticationStates.Authenticating()) }
        checkErrors(true).also { errorMessages ->
            if (errorMessages.isEmpty()) {
                viewModelScope.launch {
                    val userId = coroutineScope {
                        userDataRepository.saveNewUser(
                            UserDomain(
                                userName = userName.value,
                                email = email.value,
                                password = password.value
                            )
                        )
                    }

                    coroutineScope {
                        if (userId > 0) {
                            doLogin(this) {
                                userDataRepository.getUser(
                                    UserDomain(
                                        userName = userName.value,
                                        email = email.value,
                                        password = password.value
                                    )
                                )
                            }
                        } else {
                            uiState.update {
                                it.copy(
                                    authState = LoginAuthenticationStates.ErrorNewAuthentication(
                                        "Error creating the new user"
                                    )
                                )
                            }
                        }
                    }
                }
            } else {
                uiState.update {
                    it.copy(
                        authState = LoginAuthenticationStates.ErrorNewAuthentication(
                            errorMessages.joinToString(separator = "\n"))
                    )
                }
            }
        }
    }

    private fun checkErrors(isSignUp: Boolean = false): ArrayList<String> {
        return arrayListOf<String>().run {
            if (userName.errMessage.isNotEmpty() && isSignUp) {
                add("Username ${userName.errMessage}")
            }
            if (email.errMessage.isNotEmpty()) {
                add("Email ${email.errMessage}")
            }
            if (password.errMessage.isNotEmpty()) {
                add("Password ${password.errMessage}")
            }
            this
        }
    }

    fun resetState() {
        _loginAuthenticationState.value = LoginAuthenticationStates.Idle
        uiState.update { it.copy(authState = LoginAuthenticationStates.Idle) }
    }

    fun moveToSignUp() {
        _navigateToSignUp.value = Event(Unit)
    }
}
