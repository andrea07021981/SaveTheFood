package com.example.savethefood.login

import android.util.Patterns
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.repository.UserRepository
import kotlinx.coroutines.*

class SignUpViewModel @ViewModelInject constructor(
    private val userDataRepository: UserRepository
) : ViewModel() {

    var user = UserDomain()
    var userNameValue = MutableLiveData<String>()
    var errorUserName = MutableLiveData<Boolean>()
    var emailValue = MutableLiveData<String>()
    var errorEmail = MutableLiveData<Boolean>()
    var passwordValue = MutableLiveData<String>()
    var errorPassword = MutableLiveData<Boolean>()

    private val _loginEvent = MutableLiveData<Event<Unit>>()
    val loginEvent: LiveData<Event<Unit>>
        get() = _loginEvent

    init {
        userNameValue.value = ""
        emailValue.value = ""
        passwordValue.value = ""
    }

    fun onSignUpClick(){
        if (!checkValues()) {
            with(user) {
                    userName = userNameValue.value.toString()
                    email = emailValue.value.toString()
                    password = passwordValue.value.toString()
                }
                .also {
                    viewModelScope.launch {
                        userDataRepository.saveNewUser(user)
                        _loginEvent.value = Event(Unit)
                    }
                }
        }
    }

    private fun checkValues (): Boolean {
        errorUserName.value = userNameValue.value.isNullOrEmpty()
        errorEmail.value = !Patterns.EMAIL_ADDRESS.matcher(emailValue.value).matches()
        errorPassword.value = passwordValue.value.isNullOrEmpty()
        return errorUserName.value!! && errorEmail.value!! && errorPassword.value!!
    }

    /**
     * Factory for constructing SignUpViewModel with parameter
     */
    class SignUpViewModelFactory(
        private val dataRepository: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SignUpViewModel(dataRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}