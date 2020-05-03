package com.example.savethefood.fooddetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.savethefood.data.domain.FoodDomain
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

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()//Must include it for livedata

    private lateinit var foodViewModel: FoodDetailViewModel

    @Before
    fun setupViewModel() {
        foodViewModel = FoodDetailViewModel(ApplicationProvider.getApplicationContext(), FoodDomain())
    }

    @Test
    fun moveToRecipeSearch_recipeSearchEvent() {
        //When adding a new task
        foodViewModel.moveToRecipeSearch(FoodDomain())

        //Then the new task event is triggered
        val value = foodViewModel.recipeFoodEvent.getOrAwaitValue()
        assertThat(
            value.getContentIfNotHandled(), not(nullValue()))
    }
}