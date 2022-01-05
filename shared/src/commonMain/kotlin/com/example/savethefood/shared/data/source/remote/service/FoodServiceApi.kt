package com.example.savethefood.shared.data.source.remote.service

import com.example.savethefood.shared.data.Result
import com.example.savethefood.shared.data.source.remote.datatransferobject.*
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.isActive
import kotlin.jvm.Volatile
import kotlin.native.concurrent.ThreadLocal

// TODO use DI to pass the HttpClient and remove the companion
/**
 * The APi calls use url for path and query for query search
 */
class FoodServiceApi(

) {

    private val baseUrl: String = "https://api.spoonacular.com/"
    private val apiKey = "dc03ecff1b4e4630b92c6cf4d7412449"
    // TODO change it, we do not need to expose itt directly
    var socket: WebSocketSession? = null

    @ThreadLocal
    companion object {
        @Volatile
        private var httpClient: HttpClient? = null

        var client: HttpClient =
            httpClient ?: KtorClientFactory().build().also {
                    httpClient = it

        }
    }

    // DSL generic to build the request
    /**
     * We could also have used:
     *  private suspend inline fun <reified T> HttpClient.apiGet(
            block: HttpRequestBuilder.() -> Unit = {}
        ) : T {
            val request = HttpRequestBuilder()
            request.parameter("apiKey", apiKey)
            request.block()

            OR

            with(HttpRequestBuilder()) {
                parameter("apiKey", apiKey)
                this.block()
            }
        }
     */
    private suspend inline fun <reified T> HttpClient.apiGet(
        block: HttpRequestBuilder.() -> Unit = {}
    ) : T = get {
        parameter("apiKey", apiKey)
        apply(block)
        // OR block()
    }

    suspend fun getFoodByUpc(type: String): NetworkFood {
        return client.apiGet {
            url("$baseUrl/food/products/upc/$type")
        }
    }

    suspend fun getFoodByName(query: String): NetworkFoodSearch {
        return client.apiGet {
            url("$baseUrl/food/products/search")
            parameter("query", query)
        }
    }

    suspend fun getFoodById(id: Int): NetworkFood {
        return client.apiGet {
            url("$baseUrl/food/products/$id")
        }
    }

    // TODO we need a websocket to keep listening AND STREAM DATA
    // https://github.com/philipplackner/KtorAndroidChat/blob/app/app/src/main/java/com/plcoding/ktorandroidchat/data/remote/ChatSocketServiceImpl.kt
    suspend fun getRecipes(limit: Int = 100): NetworkRecipe {
        return client.apiGet {
            url("$baseUrl/recipes/search")
            parameter("number", limit)
        }
    }

    suspend fun getRecipesById(id: Int): NetworkRecipe? {
        return client.apiGet {
            url("$baseUrl/recipes/search")
            parameter("id", id)
        }
    }

    suspend fun getRecipesByIngredient(ingredients: String?): List<NetworkRecipeIngredientsItem> {
        return client.apiGet {
            url("$baseUrl/recipes/findByIngredients")
            parameter("ingredients", ingredients)
        }
    }

    //@GET("recipes/{id}/information")
    suspend fun getRecipeInfo(id: Int): NetworkRecipeInfo {
        return client.apiGet {
            url("$baseUrl/recipes/$id/information")
        }
    }


    /**
     * Call this method to open a socket at a specific url
     */
    suspend fun initSocketSession(url: String = "$baseUrl/recipes/search"): Result<Unit> {
        return try {
            socket = client.webSocketSession {
                url(url)
            }
            if(socket?.isActive == true) {
                Result.Success(Unit)
            } else Result.Error("Couldn't establish a connection.")
        } catch(e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun closeSession() {
        socket?.close()
    }

}