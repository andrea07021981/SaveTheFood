package com.example.savethefood.login

import android.util.Patterns
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.repository.UserRepository
import kotlinx.coroutines.*

//Inherit from AndroidViewModel we don't need to use a CustomViewmodelFactory for passing the application
class SignUpViewModel(
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

    private var viewModelJob = Job()
    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this scope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        userNameValue.value = ""
        emailValue.value = ""
        passwordValue.value = ""
    }

    fun onSignUpClick(){
        if (!checkValues()) {
            user
                .apply {
                    userName = userNameValue.value.toString()
                    userEmail = emailValue.value.toString()
                    userPassword = passwordValue.value.toString()
                }
                .also {
                    uiScope.launch {
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Factory for constructing SignUpViewModel with parameter
     */
    class SignUpViewModelFactory(
        private val dataRepository: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(dataRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}