package com.example.savethefood.login;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.savethefood.viewmodel.getOrAwaitValue
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4::class)
class LoginViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()//Must include it for livedata

    lateinit var loginViewModel: LoginViewModel
    lateinit var userName: String
    lateinit var userPassword: String

    @Before
    fun setupViewModel() {
        loginViewModel = LoginViewModel(ApplicationProvider.getApplicationContext())
        userName = "a@a.com"
        userPassword = "a"
    }

    @Test
    fun onSignUpClick_credentialsOk() {

        // When trying to log in with valid credentials
        //TODO add coroutine test detail
        loginViewModel.emailValue.value = userName
        loginViewModel.passwordValue.value = userPassword
        loginViewModel.onSignUpClick()

        // Then it retrieves a valid user
        val value = loginViewModel.userLogged.getOrAwaitValue()
        assertThat(value, not(nullValue()))
    }
}