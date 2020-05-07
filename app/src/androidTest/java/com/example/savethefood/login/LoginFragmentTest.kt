package com.example.savethefood.login

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.savethefood.R
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.local.datasource.FakeUserDataSourceTest
import com.example.savethefood.data.source.repository.FakeUserDataRepositoryTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class LoginFragmentTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()//Must include it for livedata

    private lateinit var repository: FakeUserDataRepositoryTest
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun initRepository() {
        repository = FakeUserDataRepositoryTest(FakeUserDataSourceTest())
        loginViewModel = LoginViewModel(repository)
            .apply {
                emailValue.value = "a@a"
                passwordValue.value = "a"
            }
        val user1 = UserDomain("a", "a@a", "a")
        val user2 = UserDomain("b", "a@b", "b")
        val user3 = UserDomain("c", "a@c", "c")
        val user4 = UserDomain("d", "a@d", "d")
        runBlocking {  repository.addUsers(user1, user2, user3, user4) } // Simulate a coroutine

    }

    @Test
    fun activeLogin_DisplayedInUi() = runBlockingTest {
        launchFragmentInContainer<LoginFragment>(null, R.style.AppTheme)
        Thread.sleep(2000)

        onView(withId(R.id.etEmail))
            .check(matches(isDisplayed()))

        onView(withId(R.id.etPassword))
            .check(matches(isDisplayed()))

        onView(withId(R.id.button)).perform(click())
    }

    @Test
    fun clickLogin_navigateToHome() = runBlockingTest{
        //GIVEN - A login screen and some users on fake data
        val scenario = launchFragmentInContainer<LoginFragment>(Bundle(), R.style.AppTheme)

        //Create a navcontroller and attach it to the current fragment scenario
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            it.view?.let { view ->
                    Navigation.setViewNavController(view, navController)
            }
        }

        //WHEN - User click login with right credentials
        onView(withId(R.id.button)).perform(click())

        //THEN - navigate to home fragment (the nested is just a container for sub navigation) with the current user
         verify(navController)
             .navigate(LoginFragmentDirections.actionLoginFragmentToNestedNavGraph(UserDomain()))
    }
}