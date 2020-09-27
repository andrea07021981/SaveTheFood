package com.example.savethefood.recipe

import android.os.Build
import android.os.Looper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.savethefood.MainCoroutineRule
import com.example.savethefood.constants.Done
import com.example.savethefood.constants.Error
import com.example.savethefood.constants.Loading
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.local.datasource.FakeRecipeDataSourceTest
import com.example.savethefood.data.source.local.datasource.FakeUserDataSourceTest
import com.example.savethefood.data.source.repository.FakeRecipeDataRepositoryTest
import com.example.savethefood.data.source.repository.FakeUserDataRepositoryTest
import com.example.savethefood.viewmodel.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@LooperMode(LooperMode.Mode.PAUSED)
class RecipeViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()//Must include it for livedata
    //This swap the standard coroutine main dispatcher to the test dispatcher
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var fakeRecipeDataRepositoryTest: FakeRecipeDataRepositoryTest

    @Before
    fun setupViewModel() {
        val recipeList = arrayListOf(
            RecipeResult(1, "w", "W", "W", 1, 1, "w"),
            RecipeResult(2, "w", "W", "W", 1, 1, "w"),
            RecipeResult(3, "w", "W", "W", 1, 1, "w"),
            RecipeResult(4, "w", "W", "W", 1, 1, "w"),
            RecipeResult(5, "w", "W", "W", 1, 1, "w"),
            RecipeResult(6, "w", "W", "W", 1, 1, "w")
        )
        fakeRecipeDataRepositoryTest = FakeRecipeDataRepositoryTest(FakeRecipeDataSourceTest(recipeList))
        recipeViewModel = RecipeViewModel(fakeRecipeDataRepositoryTest, "")
    }

    @Test
    fun moveToRecipeDetail_detailRecipeEvent() {
        //When adding a new task
        recipeViewModel.moveToRecipeDetail(RecipeResult())

        //Then the new task event is triggered
        val value = recipeViewModel.recipeDetailEvent.getOrAwaitValue()
        assertThat(
            value.getContentIfNotHandled(), not(nullValue()))
    }

    @Test
    fun updateDataList_listOfRecipes_returnEvent() {
        // runBlockingTest gives you finer control over virtual time if you need it
        // runBlocking is good for testing non-delays
        mainCoroutineRule.runBlockingTest {
            recipeViewModel.searchFilter.value = ""
            //Given a list of results
            val flowRequest = fakeRecipeDataRepositoryTest.getRecipes("")
                .onStart {
                }
                .catch {
                }
                .transform { value ->
                    if (value is Result.Success) {
                        emit(value.data.results)
                    }
                }
                .onCompletion {
                }

            // WHEN you update the list,
            recipeViewModel._recipeListResult =
                flowRequest
                    .asLiveData()
            //Then the new task event is triggered and the array is not empty
            recipeViewModel.recipeListResult.observeForever {
                assertFalse(it!!.isNotEmpty())
            }
        }
    }

    @After
    fun close() {

    }
}