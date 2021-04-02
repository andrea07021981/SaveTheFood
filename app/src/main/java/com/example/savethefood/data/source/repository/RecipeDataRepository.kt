package com.example.savethefood.data.source.repository

import android.app.Application
import com.example.savethefood.constants.ApiCallStatus
import com.example.savethefood.data.Result
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.source.RecipeDataSource
import com.example.savethefood.data.source.local.datasource.RecipeLocalDataSource
import com.example.savethefood.data.source.local.entity.asDomainModel
import com.example.savethefood.data.source.remote.datasource.RecipeRemoteDataSource
import com.example.savethefood.data.source.remote.datatransferobject.asDomainModel
import com.example.savethefood.data.source.remote.service.ApiClient
import com.example.savethefood.util.wrapEspressoIdlingResource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class RecipeDataRepository @Inject constructor(
    private val recipeLocalDataSource: RecipeDataSource,
    private val recipeRemoteDataSource: RecipeDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RecipeRepository {

    @Throws(Exception::class)
    override fun getRecipes(foodFilter: String?): Flow<Result<RecipeDomain>> {
        return wrapEspressoIdlingResource {
            // TODO use CacheOnSuccess like advance coroutines with oneach and change from flow to suspend and coroutine
            recipeRemoteDataSource.getRecipes(foodFilter)
                .map {
                    if (it != null) {
                        Result.Success(it)
                    } else {
                        Result.Error("No data")
                    }
                }
                .retryWhen {cause, attempt ->
                    if (cause is IOException && attempt < 5) {    // retry on IOException
                        delay(1000)                     // delay for one second before retry
                        true
                    } else {                                      // do not retry otherwise
                        false
                    }
                }
                .flowOn(ioDispatcher)
        }
    }

    @Throws(Exception::class)
    override suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain> = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            recipeRemoteDataSource.getRecipeInfo(id)
        }
    }

    override suspend fun saveRecipe(recipe: RecipeInfoDomain) = withContext(ioDispatcher){
        wrapEspressoIdlingResource {
            recipeLocalDataSource.saveRecipe(recipe)
        }
    }
}