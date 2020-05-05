package com.example.savethefood.login

import androidx.lifecycle.*
import com.example.savethefood.R
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userDataRepository: UserRepository
) : ViewModel() {

    val animationResourceView = R.anim.fade_in
    val animationResourceButton = R.anim.bounce

    var emailValue = MutableLiveData<String>()
    var passwordValue = MutableLiveData<String>()
    var errorPassword = MutableLiveData<Boolean>()
    var errorEmail = MutableLiveData<Boolean>()

    private val _userLogged = MediatorLiveData<Result<UserDomain>>()
    val userLogged: LiveData<Result<UserDomain>>
        get() = _userLogged

    private val _navigateToSignUpFragment = MutableLiveData<Boolean>()
    val navigateToSignUpFragment: LiveData<Boolean>
        get() = _navigateToSignUpFragment

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

    private fun doLogin() = uiScope.launch {
        val userToSave = UserDomain()
            .apply {
            userEmail = emailValue.value.toString()
            userPassword = passwordValue.value.toString()
        }
        val userRecord = userDataRepository.getUser(user = userToSave)
        _userLogged.value = userRecord
    }

    fun doneNavigationSignUp() {
        _navigateToSignUpFragment.value = null
    }

    fun moveToSignUp() {
        _navigateToSignUpFragment.value = true
    }

    fun doneNavigationHome() {
        _userLogged.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Factory for constructing LoginViewModel with parameter
     */
    class LoginViewModelFactory(
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