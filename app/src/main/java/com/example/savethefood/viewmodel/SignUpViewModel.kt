package com.example.savethefood.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.*
import com.example.savethefood.local.database.SaveTheFoodDatabase
import com.example.savethefood.local.domain.UserDomain
import com.example.savethefood.repository.UserRepository
import kotlinx.coroutines.*

//Inherit from AndroidViewModel we don't need to use a CustomViewmodelFactory for passing the application
class SignUpViewModel(
    application: Application
) : AndroidViewModel(application) {

    var user = UserDomain()
    var userNameValue = MutableLiveData<String>()
    var errorUserName = MutableLiveData<Boolean>()
    var emailValue = MutableLiveData<String>()
    var errorEmail = MutableLiveData<Boolean>()
    var passwordValue = MutableLiveData<String>()
    var errorPassword = MutableLiveData<Boolean>()

    private val _navigateToLoginFragment = MutableLiveData<Boolean>()
    val navigateToLoginFragment: LiveData<Boolean>
        get() = _navigateToLoginFragment

    private val database = SaveTheFoodDatabase.getInstance(application)
    private val userRepository = UserRepository(database)
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
                        userRepository.saveNewUser(user)
                        _navigateToLoginFragment.value = true
                    }
                }
        }
    }

    fun checkValues (): Boolean {
        errorUserName.value = userNameValue.value.isNullOrEmpty()
        errorEmail.value = !Patterns.EMAIL_ADDRESS.matcher(emailValue.value).matches()
        errorPassword.value = passwordValue.value.isNullOrEmpty()
        return errorUserName.value!! && errorEmail.value!! && errorPassword.value!!
    }

    fun doneNavigating() {
        _navigateToLoginFragment.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}