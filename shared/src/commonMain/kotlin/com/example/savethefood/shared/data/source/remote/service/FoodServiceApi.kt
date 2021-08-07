package com.example.savethefood.shared.data.source.remote.service

import com.example.savethefood.shared.data.source.remote.datatransferobject.*
import io.ktor.client.*
import io.ktor.client.request.*
import kotlin.jvm.Volatile

// TODO use DI to pass the HttpClient and remove the companion
/**
 * The APi calls use url for path and query for query search
 */
class FoodServiceApi(

) {

    private val baseUrl: String = "https://api.spoonacular.com/"
    private val apiKey = "dc03ecff1b4e4630b92c6cf4d7412449"

    companion object {
        @Volatile
        private var httpClient: HttpClient? = null

        var client: HttpClient =
            httpClient ?: KtorClientFactory().build().also {
                    httpClient = it

        }
    }

    private suspend inline fun <reified T> HttpClient.apiGet(
        block: HttpRequestBuilder.() -> Unit = {}
    ) : T = get {
        parameter("apiKey", apiKey)
        apply(block)
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
}