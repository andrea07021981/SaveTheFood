package com.example.savethefood.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.savethefood.local.entity.RecipeInfoEntity

/**
 * Defines methods for using the entities class with Room.
 */
@Dao
interface RecipeInfoDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: RecipeInfoEntity): Long

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param recipe new value to write
     */
    @Update
    fun update(recipe: RecipeInfoEntity)

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM recipe_info_table")
    fun clear()

    /**
     * Selects and returns the user with given recipe id.
     */
    @Query("SELECT * from recipe_info_table WHERE id = :key")
    fun getRecipeWithId(key: Long): LiveData<RecipeInfoEntity>
}