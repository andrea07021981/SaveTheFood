package com.example.savethefood.network.service

import com.example.savethefood.network.datatransferobject.NetworkFood
import com.example.savethefood.network.datatransferobject.NetworkRecipe
import com.example.savethefood.network.service.ApiEndPoint.BASE_URL
import com.example.savethefood.network.service.ApiKey.API_KEY
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

    @GET("recipes/search")
    fun getRecipes(@Query("apiKey") key: String = API_KEY): Deferred<NetworkRecipe>

    @GET("recipes/search")
    fun getRecipeInfo(@Query("id") id: Int, @Query("apiKey") key: String = API_KEY): Deferred<NetworkRecipe>
}
