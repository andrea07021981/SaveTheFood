package com.example.savethefood.data.source.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.savethefood.data.source.local.dao.FoodDatabaseDao
import com.example.savethefood.data.source.local.dao.RecipeDatabaseDao
import com.example.savethefood.data.source.local.dao.RecipeInfoDatabaseDao
import com.example.savethefood.data.source.local.dao.UserDatabaseDao
import com.example.savethefood.data.source.local.entity.FoodEntity
import com.example.savethefood.data.source.local.entity.RecipeEntity
import com.example.savethefood.data.source.local.entity.RecipeInfoEntity
import com.example.savethefood.data.source.local.entity.UserEntity


/**
 * A database that stores food information.
 * And a global method to get access to the database.
 *
 * This pattern is pretty much the same for any database,
 * so you can reuse it.
 */
@Database(entities = [UserEntity::class, FoodEntity::class, RecipeEntity::class, RecipeInfoEntity::class], version = 2, exportSchema = false)
@TypeConverters(FoodImageConverter::class, StorageTypeConverter::class, QuantityTypeConverter::class, TimeStampConverter::class)
abstract class SaveTheFoodDatabase : RoomDatabase() {

    /**
     * Connects the database to the DAO.
     */
    abstract val userDatabaseDao: UserDatabaseDao

    abstract val foodDatabaseDao: FoodDatabaseDao

    abstract val recipeDatabaseDao: RecipeDatabaseDao

    abstract val recipeInfoDatabaseDao: RecipeInfoDatabaseDao

    /**
     * Define a companion object, this allows us to add functions on the SaveTheFoodDatabase class.
     *
     * For example, clients can call `SaveTheFoodDatabase.getInstance(context)` to instantiate
     * a new SaveTheFoodDatabase.
     */
    companion object {
        /**
         * INSTANCE will keep a reference to any database returned via getInstance.
         *
         * This will help us avoid repeatedly initializing the database, which is expensive.
         *
         *  The value of a volatile variable will never be cached, and all writes and
         *  reads will be done to and from the main memory. It means that changes made by one
         *  thread to shared data are visible to other threads.
         */
        @Volatile
        private lateinit var INSTANCE: SaveTheFoodDatabase

        /**
         * Helper function to get the database.
         *
         * If a database has already been retrieved, the previous database will be returned.
         * Otherwise, create a new database.
         *
         * This function is threadsafe, and callers should cache the result for multiple database
         * calls to avoid overhead.
         *
         * This is an example of a simple Singleton pattern that takes another Singleton as an
         * argument in Kotlin.
         *
         * To learn more about Singleton read the wikipedia article:
         * https://en.wikipedia.org/wiki/Singleton_pattern
         *
         * @param context The application context Singleton, used to get access to the filesystem.
         */
        fun getInstance(context: Context): SaveTheFoodDatabase {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a
            // time.
            synchronized(this) {

                if (!Companion::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        SaveTheFoodDatabase::class.java,
                        "save_the_food_database"
                    )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                        //.fallbackToDestructiveMigration()
                        .addMigrations(MIGRATION_1_2)
                        .build()
                }
                return INSTANCE
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS food_table (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, img_url TEXT NOT NULL)")
            }
        }

        private val MIGRATION_1_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS recipe_info_table " +
                            "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "name TEXT NOT NULL, " +
                            "img_url TEXT NOT NULL)")
            }
        }
    }
}
