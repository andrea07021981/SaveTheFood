package com.example.savethefood.home

import com.example.savethefood.data.source.remote.service.ApiEndPoint
import com.example.savethefood.data.source.remote.service.FoodService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.mockwebserver.MockWebServer

class ApiClientTest {

    companion object {
        /**
         * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
         * full Kotlin compatibility.
         */
        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        private val okHttpClientBuilder = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(getInterceptior())

        private fun getInterceptior(): HttpLoggingInterceptor {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            return logging
        }

        fun buildRetrofitTest(mockWebServer: MockWebServer): FoodService {
            return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .baseUrl(mockWebServer.url("/"))
                .client(okHttpClientBuilder.build())
                .build()
                .create(FoodService::class.java)
        }

        /**
         * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
         * object.
         */
        private val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(ApiEndPoint.BASE_URL)
            .client(okHttpClientBuilder.build())
            .build()

        /**
         * A public Api object that exposes the lazy-initialized Retrofit service
         */
        val retrofitService : FoodService by lazy { retrofit.create(
            FoodService::class.java) }
    }
}