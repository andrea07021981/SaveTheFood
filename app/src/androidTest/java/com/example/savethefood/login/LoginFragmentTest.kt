package com.example.savethefood.login

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.savethefood.R
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.local.datasource.FakeUserDataSourceTest
import com.example.savethefood.data.source.repository.FakeUserDataRepositoryTest
import com.example.savethefood.data.source.repository.RecipeRepository
import com.example.savethefood.data.source.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.AllOf.allOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class LoginFragmentTest {

    private lateinit var repository: FakeUserDataRepositoryTest

    @Before
    fun initRepository() {
        repository = FakeUserDataRepositoryTest(FakeUserDataSourceTest())
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
}