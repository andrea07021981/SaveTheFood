package com.example.savethefood.fooddetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.local.datasource.FakeFoodDataSourceTest
import com.example.savethefood.data.source.repository.FakeFoodDataRepositoryTest
import com.example.savethefood.home.HomeViewModel
import com.example.savethefood.viewmodel.getOrAwaitValue
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FoodDetailViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()//Must include it for livedata

    //TODO add fake data and repository for food and recipes

    private lateinit var fakeFoodDataRepositoryTest: FakeFoodDataRepositoryTest
    private lateinit var foodDetailViewModel: FoodDetailViewModel

    @Before
    fun setupViewModel() {
        fakeFoodDataRepositoryTest = FakeFoodDataRepositoryTest(FakeFoodDataSourceTest())
        foodDetailViewModel = FoodDetailViewModel(fakeFoodDataRepositoryTest, FoodDomain())
    }

    @Test
    fun moveToRecipeSearch_recipeSearchEvent() {
        //When adding a new task
        foodDetailViewModel.moveToRecipeSearch(FoodDomain())

        //Then the new task event is triggered
        val value = foodDetailViewModel.recipeFoodEvent.getOrAwaitValue()
        assertThat(
            value.getContentIfNotHandled(), not(nullValue()))
    }
}