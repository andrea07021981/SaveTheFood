package com.example.savethefood.login

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.savethefood.MainCoroutineRuleAndroid
import com.example.savethefood.R
import com.example.savethefood.data.source.local.datasource.FakeUserDataSourceTest
import com.example.savethefood.data.source.repository.FakeUserDataRepositoryTest
import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.utils.LoginAuthenticationStates
import com.example.savethefood.util.DataBindingIdlingResource
import com.example.savethefood.util.EspressoIdlingResource
import com.example.savethefood.util.monitorFragment
import com.example.savethefood.viewmodel.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
@ExperimentalCoroutinesApi
class LoginFragmentTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()//Must include it for livedata
    @get:Rule
    var mainCoroutineRule = MainCoroutineRuleAndroid()

    private lateinit var repository: FakeUserDataRepositoryTest
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun initRepository() {
        repository = FakeUserDataRepositoryTest(FakeUserDataSourceTest())
        loginViewModel = LoginViewModel(repository)
        val user1 = UserDomain("a", "a@a", "a")
        val user2 = UserDomain("b", "a@b", "b")
        val user3 = UserDomain("c", "a@c", "c")
        val user4 = UserDomain("d", "a@d", "d")
        mainCoroutineRule.runBlockingTest {  repository.addUsers(user1, user2, user3, user4) } // Simulate a coroutine

    }

    //We are using this class for databinding test
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun activeLogin_DisplayedInUi() = mainCoroutineRule.runBlockingTest {
        // GIVEN a fragment
        val fragmentScenario =
            launchFragmentInContainer<LoginFragment>(null, R.style.AppTheme)
        Thread.sleep(2000)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        // _WHEN email and pass are displayed
        onView(withId(R.id.etEmail)).perform(clearText(), typeText("a@a.com"))
        onView(withId(R.id.etPassword)).perform(clearText(), typeText("a"))
        onView(withId(R.id.etEmail))
            .check(matches(isDisplayed()))

        onView(withId(R.id.etPassword))
            .check(matches(isDisplayed()))

        // THEN click and login
        onView(withId(R.id.login_button)).perform(click())
        val value = loginViewModel.loginAuthenticationState.getOrAwaitValue()
        assertThat(value, CoreMatchers.`is`(LoginAuthenticationStates.InvalidAuthentication("Not found")))
    }

    @Test
    fun clickLogin_navigateToHome() {
    }
}