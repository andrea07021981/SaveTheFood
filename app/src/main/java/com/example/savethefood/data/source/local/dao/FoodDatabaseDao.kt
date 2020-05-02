package com.example.savethefood.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.savethefood.data.source.local.entity.FoodEntity

/**
 * Defines methods for using the entities class with Room.
 */
@Dao
interface FoodDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(food: FoodEntity) : Long

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param user new value to write
     */
    @Update
    fun update(food: FoodEntity)

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM food_table")
    fun clear()

    /**
     * Delete a single food
     */
    @Delete
    fun deleteFood(food: FoodEntity)

    /**
     * Selects and returns the user with given userId.
     */
    @Query("SELECT * from food_table WHERE id = :key")
    fun getFoodWithId(key: Long): LiveData<FoodEntity>

    /**
     * Selects all food
     */
    @Query("SELECT * from food_table")
    fun getFoods(): LiveData<List<FoodEntity>>
}