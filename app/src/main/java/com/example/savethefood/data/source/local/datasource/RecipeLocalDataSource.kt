package com.example.savethefood.data.source.local.datasource

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.*
import com.example.savethefood.data.source.RecipeDataSource
import com.example.savethefood.data.source.local.dao.RecipeDatabaseDao
import com.example.savethefood.data.source.local.entity.RecipeEntity
import com.example.savethefood.data.source.local.entity.asDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecipeLocalDataSource @Inject constructor(
    private val recipeDatabaseDao: RecipeDatabaseDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RecipeDataSource {

    @Throws(Exception::class)
    override fun getRecipes(): Flow<RecipeDomain?> {
        TODO("No OP")
    }

    @Throws(Exception::class)
    override fun getRecipesByIngredients(vararg foodFilter: String?): Flow<List<RecipeIngredients>?> {
        TODO("Not yet implemented")
    }

    @Throws(Exception::class)
    override suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain> = coroutineScope {
        TODO("No OP")
    }

    override suspend fun saveRecipe(recipe: RecipeIngredients): RecipeIngredients? {
        val newRowId = recipeDatabaseDao.insertRecipe(recipe.asDatabaseModel())
        if (newRowId > 0) return recipe
        return null
    }

    override suspend fun getRecipeIngredients(recipeId: Int): RecipeIngredients? {
        return recipeDatabaseDao.getRecipe(recipeId)?.asDomainModel()
    }

    override fun getRecipesIngredients(): Flow<List<RecipeIngredients>?> {
        return recipeDatabaseDao.getRecipes().asDomainModel()
    }

    override suspend fun deleteRecipe(recipeId: RecipeIngredients): Int {
        return recipeDatabaseDao.deleteRecipe(recipeId.asDatabaseModel())
    }
}