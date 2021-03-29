package com.example.savethefood.data.source.repository

import android.app.Application
import com.example.savethefood.data.Result
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.source.RecipeDataSource
import com.example.savethefood.data.source.local.datasource.RecipeLocalDataSource
import com.example.savethefood.data.source.remote.datasource.RecipeRemoteDataSource
import com.example.savethefood.data.source.remote.datatransferobject.asDomainModel
import com.example.savethefood.data.source.remote.service.ApiClient
import com.example.savethefood.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
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
            // TODO use CacheOnSuccess like advance coroutines and change from flow to suspend and coroutine
            recipeRemoteDataSource.getRecipes(foodFilter)
                .transform { value ->
                    if (value != null) {
                        emit(Result.Success(value))
                    } else {
                        emit(Result.Error("No data"))
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