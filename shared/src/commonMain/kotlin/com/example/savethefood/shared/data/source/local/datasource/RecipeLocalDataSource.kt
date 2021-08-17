package com.example.savethefood.shared.data.source.local.datasource

import com.example.savethefood.shared.cache.SaveTheFoodDatabase
import com.example.savethefood.shared.cache.SaveTheFoodDatabaseQueries
import com.example.savethefood.shared.data.Result
import com.example.savethefood.shared.data.domain.RecipeDomain
import com.example.savethefood.shared.data.domain.RecipeInfoDomain
import com.example.savethefood.shared.data.domain.RecipeIngredients
import com.example.savethefood.shared.data.domain.asDatabaseModel
import com.example.savethefood.shared.data.source.RecipeDataSource
import com.example.savethefood.shared.data.source.local.entity.RecipeIngredientEntity
import com.example.savethefood.shared.data.source.local.entity.asDomainModel
import com.example.savethefood.shared.data.source.local.entity.asRecipeDomainModel
import com.example.savethefood.shared.utils.mapToRecipeEntity
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class RecipeLocalDataSource(
    private val foodDatabase: SaveTheFoodDatabase
) : RecipeDataSource {

    private val dbQuery: SaveTheFoodDatabaseQueries = foodDatabase.saveTheFoodDatabaseQueries

    @Throws(Exception::class)
    override fun getRecipes(): Flow<RecipeDomain?> {
        return  dbQuery.selectRecipes(::mapToRecipeEntity)
            .asFlow()
            .mapToList()
            .asRecipeDomainModel()
    }

    override suspend fun getRecipeById(id: Int): RecipeDomain? {
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

    override suspend fun saveRecipe(recipe: RecipeIngredients): Long? {
        return recipe.asDatabaseModel().run {
            dbQuery.transactionWithResult {
                dbQuery.insertRecipe(
                    recipeId = recipeId,
                    id = id,
                    title = title,
                    image = image,
                    imageType = imageType,
                    likes = likes.toLong(),
                    missedIngredientCount = missedIngredientCount.toLong(),
                    usedIngredientCount = usedIngredientCount.toLong(),
                    unUsedIngredientCount = unUsedIngredientCount.toLong(),
                )
                dbQuery.lastInsertRowId().executeAsOne()
            }
        }
    }

    override suspend fun getRecipeIngredients(recipeId: Int): RecipeIngredients {
        return dbQuery.selectRecipeById(recipeId.toLong(), ::mapToRecipeEntity).executeAsOne().asDomainModel()
    }

    override fun getRecipesIngredients(): Flow<List<RecipeIngredients>?> {
        return dbQuery.selectRecipes(::mapToRecipeEntity).asFlow().mapToList().asDomainModel()
    }

    override suspend fun deleteRecipe(recipeId: RecipeIngredients): Long? {
        return withContext(Dispatchers.Default) {
            dbQuery.transactionWithResult {
                dbQuery.deleteRecipeById(recipeId.id)
                dbQuery.changes().executeAsOneOrNull()
            }
        }
    }
}