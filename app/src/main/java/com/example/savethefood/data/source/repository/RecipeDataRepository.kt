package com.example.savethefood.data.source.repository

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.domain.RecipeIngredients
import com.example.savethefood.data.source.RecipeDataSource
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
    override fun getRecipes(): Flow<Result<RecipeDomain>> {
        return wrapEspressoIdlingResource {
            // TODO use CacheOnSuccess like advance coroutines with oneach and change from flow to suspend and coroutine
            recipeRemoteDataSource.getRecipes()
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

    override fun getRecipesByIngredients(vararg foodFilter: String?): Flow<Result<List<RecipeIngredients>?>> {
        return wrapEspressoIdlingResource {
            // TODO use CacheOnSuccess like advance coroutines with oneach and change from flow to suspend and coroutine
            // TODO add a custom property and search in db for save recipes, we need them for the heart icon
            recipeRemoteDataSource.getRecipesByIngredients(*foodFilter)
                .map { list ->
                    list?.let {
                        if (it.count() > 0) {
                            Result.Success(it)
                        } else {
                            Result.Error("No data")
                        }
                    } ?: Result.ExError(Exception("Error retrieving data"))

                }
                .retryWhen {cause, attempt ->
                    if (cause is IOException && attempt < 5) {    // retry on IOException
                        delay(10000)                     // delay for one second before retry
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

    override suspend fun saveRecipe(recipe: RecipeIngredients) = withContext(ioDispatcher){
        wrapEspressoIdlingResource {
            // TODO check if we are are saving or deleting, WE just need ot check if we have a record
            // if exist, delete otherwise :
            // TODO Retrieve the network recipe by id and save locally

            val dbRecipe = recipeLocalDataSource.getRecipe(recipe.id)
            // If present, remove from favourites and return
            return@withContext dbRecipe?.run {
                recipeLocalDataSource.deleteRecipe(recipe)
                Result.Success(null)
            } ?: kotlin.run {
                val newRecipe = recipeLocalDataSource.saveRecipe(recipe)
                newRecipe?.let {
                    Result.Success(it)
                } ?: Result.Error("No inserted")
            }
        }
    }
}