package com.example.savethefood.data.source.local.dao

import androidx.room.*
import com.example.savethefood.data.source.local.entity.RecipeIngredientEntity
import kotlinx.coroutines.flow.Flow


/**
 * Defines methods for using the entities class with Room.
 */
@Dao
interface RecipeDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeIngredientEntity): Long

    @Transaction
    @Query("SELECT * FROM RecipeIngredient WHERE id = :id")
    suspend fun getRecipe(id: Int): RecipeIngredientEntity?

    @Transaction
    @Query("SELECT * FROM RecipeIngredient")
    fun getRecipes(): Flow<List<RecipeIngredientEntity>?>

    @Delete
    suspend fun deleteRecipe(food: RecipeIngredientEntity): Int
}
