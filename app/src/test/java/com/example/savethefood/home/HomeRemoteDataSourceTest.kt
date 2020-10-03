package com.example.savethefood.home

import android.app.Application
import android.os.Looper
import android.service.autofill.Validators.not
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import com.example.savethefood.FileReader
import com.example.savethefood.SaveTheFoodApplication
import com.example.savethefood.data.source.remote.service.ApiClient
import com.example.savethefood.data.source.remote.service.FoodService
import com.jakewharton.espresso.OkHttp3IdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.LooperMode
import java.io.File
import java.net.HttpURLConnection


@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class HomeRemoteDataSourceTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mockWebServer = MockWebServer()
    private lateinit var apiService: FoodService

    @Before
    fun setUp() {
        mockWebServer.start()
        IdlingRegistry.getInstance().register(
            OkHttp3IdlingResource.create(
                "okhttp",
                OkHttpProvider.getOkHttpClient(
                    (InstrumentationRegistry.getInstrumentation().targetContext
                        .applicationContext as SaveTheFoodApplication)
                )
            )
        )
        apiService = ApiClientTest.buildRetrofitTest(mockWebServer)
    }

    @Test
    fun testOffLine_Success() {
        // GIVEN
        val fileName = "success_response.json"
        var response: MockResponse? = null
        // WHEN
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                response = MockResponse()
                    .setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("success_response.json"))
                return response!!
            }
        }
        //THEN
        assertThat(response?.getBody().toString(), IsNull.notNullValue())
    }

    @Test
    fun testOnLine_Success() = runBlocking {
        // GIVEN
        // WHEN
        //THEN
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(FileReader.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(response)
        shadowOf(Looper.getMainLooper()).idle()
        val deferred = async {
            apiService.getFoodByUpc("1111")
        }.await()

        val result = deferred.await() // result available immediately
        assertThat(result.id, CoreMatchers.`is`(30004))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}

class OkHttpProvider {

    companion object {

        fun getOkHttpClient(app: Application): OkHttpClient {
            // Install an HTTP cache in the application cache directory.
            val cacheDir = File(app.cacheDir, "http")
            val cache = Cache(cacheDir, 2000)
            return OkHttpClient.Builder()
                .cache(cache).build()
        }
    }
}

