package com.example.savethefood.data.source.repository

import android.app.Application
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.source.RecipeDataSource
import com.example.savethefood.data.source.local.datasource.RecipeLocalDataSource
import com.example.savethefood.data.source.remote.datasource.RecipeRemoteDataSource
import com.example.savethefood.data.source.remote.datatransferobject.asDomainModel
import com.example.savethefood.data.source.remote.service.ApiClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.lang.Exception

class RecipeDataRepository(
    private val recipeLocalDataSource: RecipeDataSource,
    private val recipeRemoteDataSource: RecipeDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RecipeRepository {

    companion object {
        @Volatile
        private var INSTANCE: RecipeDataRepository? = null

        fun getRepository(app: Application): RecipeDataRepository{
            return INSTANCE ?: synchronized(this) {
                val database = SaveTheFoodDatabase.getInstance(app)
                return RecipeDataRepository(
                    RecipeLocalDataSource(database.recipeDatabaseDao),
                    RecipeRemoteDataSource(ApiClient.retrofitService)
                ).also {
                    INSTANCE = it
                }
            }
        }
    }

    @Throws(Exception::class)
    override suspend fun getRecipes(foodFilter: String?): RecipeDomain = withContext(ioDispatcher) {
        recipeRemoteDataSource.getRecipes(foodFilter)
    }

    @Throws(Exception::class)
    override suspend fun getRecipeInfo(id: Int): RecipeInfoDomain = withContext(ioDispatcher) {
        recipeRemoteDataSource.getRecipeInfo(id)
    }

    override suspend fun saveRecipe(recipe: RecipeInfoDomain) = withContext(ioDispatcher){
        recipeLocalDataSource.saveRecipe(recipe)
    }
}