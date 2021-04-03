package com.example.savethefood.data.source.remote.service

import com.example.savethefood.data.source.remote.datatransferobject.*
import com.example.savethefood.data.source.remote.service.ApiEndPoint.BASE_URL
import com.example.savethefood.data.source.remote.service.ApiKey.API_KEY
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface FoodService {

    @GET("food/products/upc/{upc}")
    fun getFoodByUpc(@Path("upc") type: String, @Query("apiKey") key: String = API_KEY): Deferred<NetworkFood>

    @GET("food/products/search")
    suspend fun getFoodByName(@Query("apiKey") key: String = API_KEY, @Query("query") query: String): NetworkFoodSearch

    @GET("food/products/{id}")
    suspend fun getFoodById(@Path("id") id: Int, @Query("apiKey") key: String = API_KEY): NetworkFood

    /**
     * New direct method added in retrofit 2.6. No need enqueue and retrofit with deferred
     * it's the same as PlantApi.retrofitService.postCommand(plantId = plantDomain.plantId, commandType = commandType).await().asDomainModel()
     * and retrofit methid deferred
     */
    @GET("recipes/search")
    suspend fun getRecipes(@Query("apiKey") key: String = API_KEY, @Query("number") number: Int = 100): NetworkRecipe

    @GET("recipes/findByIngredients")
    suspend fun getRecipesByIngredient(@Query("ingredients") ingredients: String?, @Query("apiKey") key: String = API_KEY): List<NetworkRecipeIngredientsItem>

    @GET("recipes/{id}/information")
    fun getRecipeInfo(@Path("id") id: Int, @Query("apiKey") key: String = API_KEY): Deferred<NetworkRecipeInfo>
}
