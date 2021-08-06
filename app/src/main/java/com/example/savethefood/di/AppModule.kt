package com.example.savethefood.di

import android.content.Context
import androidx.room.Room
import com.example.savethefood.BuildConfig
import com.example.savethefood.data.source.FoodDataSource
import com.example.savethefood.data.source.RecipeDataSource
import com.example.savethefood.data.source.ShoppingDataSource
import com.example.savethefood.data.source.local.dao.FoodDatabaseDao
import com.example.savethefood.data.source.local.dao.RecipeDatabaseDao
import com.example.savethefood.data.source.local.dao.ShoppingDatabaseDao
import com.example.savethefood.data.source.local.dao.UserDatabaseDao
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.source.local.datasource.FoodLocalDataSource
import com.example.savethefood.data.source.local.datasource.RecipeLocalDataSource
import com.example.savethefood.data.source.local.datasource.ShoppingLocalDataSource
import com.example.savethefood.data.source.remote.datasource.FoodRemoteDataSource
import com.example.savethefood.data.source.remote.datasource.RecipeRemoteDataSource
import com.example.savethefood.data.source.remote.datasource.ShoppingRemoteDataSource
import com.example.savethefood.data.source.remote.service.FoodService
import com.example.savethefood.data.source.repository.*
import com.example.savethefood.shared.*
import com.example.savethefood.shared.data.source.UserDataSource
import com.example.savethefood.shared.data.source.local.database.DatabaseDriverFactory
import com.example.savethefood.shared.data.source.local.database.DatabaseFactory
import com.example.savethefood.shared.data.source.local.datasource.UserLocalDataSource
import com.example.savethefood.shared.data.source.remote.datasource.UserRemoteDataSource
import com.example.savethefood.shared.data.source.repository.UserDataRepository
import com.example.savethefood.shared.data.source.repository.UserRepository
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object BaseModule {

    /**
     * Data sources DI
     */
    // Best way to provide multiple implementations same interface
    @Provides
    @Named("FoodRemoteDataSource")
    fun provideFoodRemoteDataSource(
        foodApi: FoodService
    ): FoodDataSource {
        return FoodRemoteDataSource(
            foodApi
        )
    }

    @Provides
    @Named("FoodLocalDataSource")
    fun provideFoodLocalDataSource(
        foodDatabaseDao: FoodDatabaseDao
    ): FoodDataSource {
        return FoodLocalDataSource(
            foodDatabaseDao
        )
    }

    @Singleton
    @Provides
    fun provideFoodDataRepository(
        @Named("FoodLocalDataSource") foodLocalDataSource: FoodDataSource,
        @Named("FoodRemoteDataSource") foodRemoteDataSource: FoodDataSource
    ) : FoodRepository {
        return FoodDataRepository(foodLocalDataSource, foodRemoteDataSource)
    }

    @Provides
    @Named("ShoppingRemoteDataSource")
    fun provideShoppingRemoteDataSource(
    ): ShoppingDataSource {
        return ShoppingRemoteDataSource(
        )
    }

    @Provides
    @Named("ShoppingLocalDataSource")
    fun provideShoppingLocalDataSource(
        shoppingDatabaseDao: ShoppingDatabaseDao
    ): ShoppingDataSource {
        return ShoppingLocalDataSource(
            shoppingDatabaseDao
        )
    }

    @Singleton
    @Provides
    fun provideShoppingDataRepository(
        @Named("ShoppingLocalDataSource") shoppingLocalDataSource: ShoppingDataSource,
        @Named("ShoppingRemoteDataSource") shoppingRemoteDataSource: ShoppingDataSource
    ) : ShoppingRepository {
        return ShoppingDataRepository(shoppingLocalDataSource, shoppingRemoteDataSource)
    }

    @Provides
    @Named("UserRemoteDataSource")
    fun provideUserRemoteDataSource() : UserDataSource {
        return UserRemoteDataSource()
    }

    @Provides
    @Named("UserLocalDataSource")
    fun provideUserLocalDataSource(
        userDatabaseDao: com.example.savethefood.shared.cache.SaveTheFoodDatabase
    ): UserDataSource {
        return UserLocalDataSource(
            userDatabaseDao
        )
    }

    @Singleton
    @Provides
    fun provideUserDataRepository(
        @Named("UserLocalDataSource") userLocalDataSource: UserDataSource,
        @Named("UserRemoteDataSource") userRemoteDataSource: UserDataSource
    ) : UserRepository {
        return UserDataRepository(userLocalDataSource, userRemoteDataSource)
    }

    @Provides
    fun provideDispatcher() = Dispatchers.IO

    @Provides
    @Named("RecipeRemoteDataSource")
    fun provideRecipeRemoteDataSource(
        foodApi: FoodService
    ): RecipeDataSource {
        return RecipeRemoteDataSource(
            foodApi
        )
    }

    @Provides
    @Named("RecipeLocalDataSource")
    fun provideRecipeLocalDataSource(
        foodDatabaseDao: RecipeDatabaseDao
    ): RecipeDataSource {
        return RecipeLocalDataSource(
            foodDatabaseDao
        )
    }

    @Singleton
    @Provides
    fun provideRecipeDataRepository(
        @Named("RecipeLocalDataSource") recipeLocalDataSource: RecipeDataSource,
        @Named("RecipeRemoteDataSource") recipeRemoteDataSource: RecipeDataSource
    ) : RecipeRepository {
        return RecipeDataRepository(recipeLocalDataSource, recipeRemoteDataSource)
    }

    // TODO temporary injection, need to fix Koin in kmm
    @Singleton
    @Provides
    fun provideDatabaseShared(
        @ApplicationContext context: Context
    ): com.example.savethefood.shared.cache.SaveTheFoodDatabase {
        return DatabaseFactory(DatabaseDriverFactory(context)).createDatabase()
    }

}

/**
 * Database DI
 */
@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): SaveTheFoodDatabase {
        return Room.databaseBuilder(
            appContext,
            SaveTheFoodDatabase::class.java,
            "save_the_food_database.db"
        ).build()
    }

    @Provides
    fun provideFoodDao(database: SaveTheFoodDatabase): FoodDatabaseDao {
        return database.foodDatabaseDao
    }

    @Provides
    fun provideUserDao(database: SaveTheFoodDatabase): UserDatabaseDao {
        return database.userDatabaseDao
    }

    @Provides
    fun provideRecipeDao(database: SaveTheFoodDatabase): RecipeDatabaseDao {
        return database.recipeDatabaseDao
    }

    @Provides
    fun provideShoppingDao(database: SaveTheFoodDatabase): ShoppingDatabaseDao {
        return database.shoppingDatabaseDao
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_FOOD_URL
    @Provides
    @Singleton
    fun provideKotlinJsonAdapterFactory(): KotlinJsonAdapterFactory = KotlinJsonAdapterFactory()

    @Provides
    @Singleton
    fun provideInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    @Provides
    @Singleton
    fun provideOkHttp(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    fun provideMoshi(kotlinJsonAdapterFactory: KotlinJsonAdapterFactory): Moshi {
        return Moshi.Builder()
            .add(kotlinJsonAdapterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory =
        MoshiConverterFactory.create(moshi)

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory, BASE_URL: String): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideRetrofitService(retrofit: Retrofit): FoodService = retrofit.create(
        FoodService::class.java)

}