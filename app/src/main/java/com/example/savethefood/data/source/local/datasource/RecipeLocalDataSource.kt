package com.example.savethefood.data.source.local.datasource

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.source.RecipeDataSource
import com.example.savethefood.data.source.local.dao.RecipeDatabaseDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

class RecipeLocalDataSource internal constructor(
    private val recipeDatabaseDao: RecipeDatabaseDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RecipeDataSource {

    @Throws(Exception::class)
    override fun getRecipes(foodFilter: String?): Flow<RecipeDomain?> {
        TODO("No OP")
    }

    @Throws(Exception::class)
    override suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain> = coroutineScope {
        TODO("No OP")
    }

    override suspend fun saveRecipe(recipe: RecipeInfoDomain) {
        //TODO SAVE LOCAL RECIPE
    }
}