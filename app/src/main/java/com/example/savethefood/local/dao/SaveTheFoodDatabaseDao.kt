package com.example.savethefood.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.savethefood.local.entity.UserEntity


/**
 * Defines methods for using the entities class with Room.
 */
@Dao
interface SaveTheFoodDatabaseDao {

    @Insert
    fun insert(user: UserEntity)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param user new value to write
     */
    @Update
    fun update(user: UserEntity)

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM user_table")
    fun clear()

    /**
     * Selects and returns the user with given userId.
     */
    @Query("SELECT * from user_table WHERE userId = :key")
    fun getUserWithId(key: Long): LiveData<UserEntity>
}

