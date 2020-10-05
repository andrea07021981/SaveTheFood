package com.example.savethefood.di

import android.content.Context
import androidx.room.Room
import com.example.savethefood.BuildConfig
import com.example.savethefood.data.source.FoodDataSource
import com.example.savethefood.data.source.local.dao.FoodDatabaseDao
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.source.local.datasource.FoodLocalDataSource
import com.example.savethefood.data.source.remote.datasource.FoodRemoteDataSource
import com.example.savethefood.data.source.remote.service.FoodService
import com.example.savethefood.data.source.repository.FoodDataRepository
import com.example.savethefood.data.source.repository.FoodRepository
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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
@InstallIn(ApplicationComponent::class)
object BaseModule {

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
        foodLocalDataSource: FoodLocalDataSource,
        foodRemoteDataSource: FoodRemoteDataSource
    ) : FoodRepository {
        return FoodDataRepository(foodLocalDataSource, foodRemoteDataSource)
    }

}

@InstallIn(ApplicationComponent::class)
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
}

@Module
@InstallIn(ApplicationComponent::class)
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