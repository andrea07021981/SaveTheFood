package com.example.savethefood.recipedetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.local.datasource.FakeRecipeDataSourceTest
import com.example.savethefood.data.source.repository.FakeRecipeDataRepositoryTest
import com.example.savethefood.viewmodel.getOrAwaitValue
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeDetailViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()//Must include it for livedata

    private lateinit var recipeDetailViewModel: RecipeDetailViewModel
    private lateinit var fakeRecipeDataRepositoryTest: FakeRecipeDataRepositoryTest

    @Before
    fun setupViewModel() {
        fakeRecipeDataRepositoryTest = FakeRecipeDataRepositoryTest(FakeRecipeDataSourceTest(listOf()))
        recipeDetailViewModel = RecipeDetailViewModel(fakeRecipeDataRepositoryTest, RecipeResult())
    }

    @Test
    fun moveToRecipeSearch_recipeSearchEvent() {
        //When adding a new task
        //recipeDetailViewModel.moveToCookDetail(RecipeInfoDomain()) //TODO create default constructor with values

        //Then the new task event is triggered
        val value = recipeDetailViewModel.recipeCookingtEvent.getOrAwaitValue()
        assertThat(
            value.getContentIfNotHandled(), not(nullValue()))
    }
}