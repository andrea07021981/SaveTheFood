package com.example.savethefood.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.savethefood.R
import com.example.savethefood.constants.*
import com.example.savethefood.local.database.SaveTheFoodDatabase
import com.example.savethefood.local.domain.User
import com.example.savethefood.local.domain.asDatabaseModel
import com.example.savethefood.local.entity.asDomainModel
import com.example.savethefood.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel(
    application: Application
) : ViewModel() {

    val animationResourceView = R.anim.fade_in
    val animationResourceButton = R.anim.bounce

    var emailValue = MutableLiveData<String>()
    var passwordValue = MutableLiveData<String>()

    private val _autenticationState = MutableLiveData<LoginAuthenticationStates>(
        Authenticated()
    )
    val autenticationState: LiveData<LoginAuthenticationStates>
        get() = _autenticationState

    private val _userLogged = MediatorLiveData<User>()
    val userLogged: LiveData<User>
        get() = _userLogged

    private val _navigateToSignUpFragment = MutableLiveData<Boolean>()
    val navigateToSignUpFragment: LiveData<Boolean>
        get() = _navigateToSignUpFragment

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

    }

    fun onSignUpClick(){
        if (emailValue.value.isNullOrBlank() || passwordValue.value.isNullOrEmpty()) {
            _autenticationState.value = InvalidAuthentication()
        } else {
            uiScope.launch {
                val userToSave = User().apply {
                    userEmail = emailValue.value.toString()
                    userPassword = passwordValue.value.toString()
                }
                val userRecord = userRepository.getUser(user = userToSave)
                if (userRecord?.value == null) {
                    _autenticationState.value = InvalidAuthentication()
                } else {
                    _userLogged.addSource(userRecord, _userLogged::setValue)
                }
            }
        }
    }

    fun doneNavigationHome() {
        _userLogged.value = null
    }

    fun doneNavigationSignUp() {
        _navigateToSignUpFragment.value = null
    }

    fun moveToSignUp() {
        _navigateToSignUpFragment.value = true
    }

    fun resetMessage() {
        _autenticationState.value = null
    }

    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}