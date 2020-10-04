package com.example.savethefood.home

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.savethefood.MainCoroutineRuleAndroid
import com.example.savethefood.R
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.UserDomain
import com.example.savethefood.data.source.local.datasource.FakeLocalFoodDataSourceTest
import com.example.savethefood.data.source.local.datasource.FakeRemoteFoodDataSourceTest
import com.example.savethefood.data.source.repository.FakeFoodDataRepositoryTest
import com.example.savethefood.data.source.repository.FoodRepository
import com.example.savethefood.login.LoginFragment
import com.example.savethefood.login.LoginFragmentDirections
import com.example.savethefood.login.LoginViewModel
import com.example.savethefood.util.DataBindingIdlingResource
import com.example.savethefood.util.EspressoIdlingResource
import com.example.savethefood.util.monitorFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock

/**
 * This is a fragment test with fake data
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    private lateinit var repository: FoodRepository
    private lateinit var homeViewModel: HomeViewModel
    //We are using this class for databinding test
    private val dataBindingIdlingResource = DataBindingIdlingResource()
    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()//Must include it for livedata
    @get:Rule
    var mainCoroutineRule = MainCoroutineRuleAndroid()

    @Before
    fun initTest() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
        repository = FakeFoodDataRepositoryTest(
            FakeRemoteFoodDataSourceTest(mutableListOf()),
            FakeLocalFoodDataSourceTest(mutableListOf()))
        homeViewModel = HomeViewModel(repository)
    }

    @Test
    fun launchFragmentAndVerifyUI() = mainCoroutineRule.runBlockingTest{
        val fragmentArgs = Bundle().apply {
                "x" to 0
                "y" to 0
            putParcelable("user", UserDomain())
        }

        val scenario = launchFragmentInContainer<HomeFragment>(fragmentArgs = fragmentArgs, R.style.AppTheme)
        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            it.view?.let { view ->
                Navigation.setViewNavController(view, navController)
            }
        }

        dataBindingIdlingResource.monitorFragment(scenario)
        // now use espresso to look for the fragment's text view and verify it is displayed
        onView(withId(R.id.food_recycleview)).check(matches(isDisplayed()));
    }

    @After
    fun closeTest() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }
}
