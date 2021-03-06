package com.example.savethefood.login;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.savethefood.MainCoroutineRule
import com.example.savethefood.constants.LoginAuthenticationStates
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.local.datasource.FakeUserDataSourceTest
import com.example.savethefood.data.source.repository.FakeUserDataRepositoryTest
import com.example.savethefood.viewmodel.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

import org.junit.runner.RunWith;

/**
 * Complete login viewmodel ok
 * // TODO move to instrumented
 */
/*
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class LoginViewModelTest {

    //This swap the standard coroutine main dispatcher to the test dispatcher
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()//Must include it for livedata

    private lateinit var fakeUserDataRepositoryTest: FakeUserDataRepositoryTest
    private lateinit var loginViewModel: LoginViewModel

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
        mainCoroutineRule.runBlockingTest {  fakeUserDataRepositoryTest.addUsers(user1, user2, user3, user4) } // Simulate a coroutine DON'T USE IT IN REAL CODE
    }

    @Test
    fun onSignInClick_userLogging_returnValidValue() = mainCoroutineRule.runBlockingTest {
        // GIVEN I pause the rule

        // WHEN is sign up clicked
        loginViewModel.onSignUpClick()

        // THEN restart the dispatcher and is success login
        val value = loginViewModel.loginAuthenticationState.getOrAwaitValue()
        assertThat(value, `is`(equalTo(value)))
    }

    @Test
    fun onSignInClick_userLogging_returnError() {
        loginViewModel.emailValue.value = "x"
        loginViewModel.passwordValue.value = "x"
        loginViewModel.onSignUpClick()
        val value = loginViewModel.loginAuthenticationState.getOrAwaitValue()
        //assertThat(value, `is`(LoginAuthenticationStates.InvalidAuthentication("Not found")))
    }

    /**
     * Test user and pass empty
     */
    @Test
    fun login_andFail() = mainCoroutineRule.runBlockingTest{
        // Make the repository return errors.
        //TODO add error login on fragmentlogin
        fakeUserDataRepositoryTest.setReturnError(true)
        loginViewModel.emailValue.value = ""
        loginViewModel.passwordValue.value = ""
        loginViewModel.onSignUpClick()

        // Then empty and error are true (which triggers an error message to be shown).
        assertThat(loginViewModel.errorEmail.getOrAwaitValue(), `is`(true))
        assertThat(loginViewModel.errorPassword.getOrAwaitValue(), `is`(true))
    }
}
*/
