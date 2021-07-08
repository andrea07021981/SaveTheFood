package com.example.savethefood.shared.data.source.local.datasource

import com.example.savethefood.shared.cache.SaveTheFoodDatabase
import com.example.savethefood.shared.cache.SaveTheFoodDatabaseQueries
import com.example.savethefood.shared.data.ActionResult
import com.example.savethefood.shared.data.domain.RecipeDomain
import com.example.savethefood.shared.data.domain.RecipeInfoDomain
import com.example.savethefood.shared.data.domain.RecipeIngredients
import com.example.savethefood.shared.data.domain.asDatabaseModel
import com.example.savethefood.shared.data.source.RecipeDataSource
import com.example.savethefood.shared.data.source.local.entity.RecipeIngredientEntity
import com.example.savethefood.shared.data.source.local.entity.UserEntity
import com.example.savethefood.shared.data.source.local.entity.asDomainModel
import com.example.savethefood.shared.data.source.local.entity.asRecipeDomainModel
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
        return flowOf(
            dbQuery.selectRecipes(::mapToRecipeEntity).executeAsList().asRecipeDomainModel()
        )
    }

    override suspend fun getRecipeById(id: Int): RecipeDomain? {
        TODO("No OP")
    }

    @Throws(Exception::class)
    override fun getRecipesByIngredients(vararg foodFilter: String?): Flow<List<RecipeIngredients>?> {
        TODO("Not yet implemented")
    }

    @Throws(Exception::class)
    override suspend fun getRecipeInfo(id: Int): ActionResult<RecipeInfoDomain> = coroutineScope {
        TODO("No OP")
    }

    override suspend fun saveRecipe(recipe: RecipeIngredients): Long? {
        return recipe.asDatabaseModel().run {
            dbQuery.transactionWithResult {
                dbQuery.insertRecipe(
                    recipeId = this@run.recipeId,
                    id = this@run.id,
                    title = this@run.title,
                    image = this@run.image,
                    imageType = this@run.imageType,
                    likes = this@run.likes.toLong(),
                    missedIngredientCount = this@run.missedIngredientCount.toLong(),
                    usedIngredientCount = this@run.usedIngredientCount.toLong(),
                    unUsedIngredientCount = this@run.unUsedIngredientCount.toLong(),
                )
                dbQuery.lastInsertRowId().executeAsOne()
            }
        }
    }

    override suspend fun getRecipeIngredients(recipeId: Int): RecipeIngredients {
        return dbQuery.selectRecipeById(recipeId.toLong(), ::mapToRecipeEntity).executeAsOne().asDomainModel()
    }

    override fun getRecipesIngredients(): Flow<List<RecipeIngredients>?> {
        return flowOf(
            dbQuery.selectRecipes(::mapToRecipeEntity).executeAsList().asDomainModel()
        )
    }

    override suspend fun deleteRecipe(recipeId: RecipeIngredients): Long? {
        return withContext(Dispatchers.Default) {
            dbQuery.transactionWithResult {
                dbQuery.deleteRecipeById(recipeId.id)
                dbQuery.changes().executeAsOneOrNull()
            }
        }
    }

    private fun mapToRecipeEntity(
        recipeId: Long = 0,
        id: Long,
        title: String,
        image: String,
        imageType:String,
        likes: Long,
        missedIngredientCount: Long,
        usedIngredientCount: Long,
        unUsedIngredientCount: Long
    ): RecipeIngredientEntity {
        return RecipeIngredientEntity(
            recipeId = recipeId,
            id = id,
            title = title,
            image = image,
            imageType = imageType,
            likes = likes.toInt(),
            missedIngredientCount = missedIngredientCount.toInt(),
            usedIngredientCount = usedIngredientCount.toInt(),
            unUsedIngredientCount = unUsedIngredientCount.toInt()
        )
    }
}