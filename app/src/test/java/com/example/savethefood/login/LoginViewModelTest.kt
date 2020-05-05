package com.example.savethefood.login;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.local.datasource.FakeUserDataSourceTest
import com.example.savethefood.data.source.repository.FakeUserDataRepositoryTest
import com.example.savethefood.viewmodel.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4::class)
class LoginViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()//Must include it for livedata

    private lateinit var fakeUserDataRepositoryTest: FakeUserDataRepositoryTest
    lateinit var loginViewModel: LoginViewModel
    lateinit var userName: String
    lateinit var userPassword: String

    @Before
    fun setupViewModel() {
        fakeUserDataRepositoryTest = FakeUserDataRepositoryTest(FakeUserDataSourceTest())
        loginViewModel = LoginViewModel(fakeUserDataRepositoryTest)
            .apply {
                emailValue.value = "a@a"
                passwordValue.value = "a"
            }
        val user1 = UserDomain("a", "a@a", "a")
        val user2 = UserDomain("b", "a@b", "b")
        val user3 = UserDomain("c", "a@c", "c")
        val user4 = UserDomain("d", "a@d", "d")
        runBlocking {  fakeUserDataRepositoryTest.addUsers(user1, user2, user3, user4) } // Simulate a coroutine
    }

    @Test
    fun onSignInClick_userLogging_returnValidValue() {
        loginViewModel.onSignUpClick()
        val value = loginViewModel.userLogged.getOrAwaitValue() as Result.Success
        assertThat(value, `is`(equalTo(value)))
    }

    @Test
    fun onSignInClick_userLogging_returnError() {
        loginViewModel.emailValue.value = "x"
        loginViewModel.passwordValue.value = "x"
        loginViewModel.onSignUpClick()
        val value = loginViewModel.userLogged.getOrAwaitValue() as Result.Error
        assertThat(value, `is`(Result.Error("Not found")))
    }
}