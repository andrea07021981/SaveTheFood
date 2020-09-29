package com.example.savethefood.home

import android.content.Context
import android.os.Looper
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.savethefood.SaveTheFoodApplication
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.asDatabaseModel
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.source.remote.service.ApiEndPoint
import com.example.savethefood.viewmodel.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.Shadows
import java.io.IOException
import java.io.InputStreamReader

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class HomeRemoteDataSourceTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        ApiEndPoint.BASE_URL
        mockWebServer.start(8080)
    }

    @Test
    fun testOffLine_Success() {
        // GIVEN
        // WHEN
        //THEN
        // TODO Check userful
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("success_response.json"))
            }
        }
    }

    @Test
    fun testOnLine_Success() {
        // GIVEN
        // WHEN
        //THEN
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
object FileReader {
    fun readStringFromFile(fileName: String): String {
        try {
            val inputStream = (InstrumentationRegistry.getInstrumentation().targetContext
                .applicationContext as SaveTheFoodApplication).assets.open(fileName)
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }
            return builder.toString()
        } catch (e: IOException) {
            throw e
        }
    }
}