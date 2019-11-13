package com.example.savethefood.network.datatransferobject

import com.example.savethefood.local.domain.Food
import com.example.savethefood.local.entity.FoodEntity
import com.squareup.moshi.JsonClass

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 */

/**
 * FoodHolder holds a list of Foods.
 *
 * This is to parse first level of our network result which looks like
 *
 * {
 *   "Foods": []
 * }
 */
@JsonClass(generateAdapter = true)
data class NetworkFoodContainer(val foods: List<NetworkFood>)

/**
 * Foods represent a devbyte that can be played.
 */
@JsonClass(generateAdapter = true)
data class NetworkFood(
    val id: Long,
    val title: String,
    val generatedText: String,
    val images: List<String>)

/**
 * Convert Network results to database objects
 */
fun NetworkFoodContainer.asDomainModel(): List<Food> {
    return foods.map {
        Food(
            foodId = it.id,
            foodName = it.title,
            foodImgUrl = it.images.get(0))
    }
}

fun NetworkFoodContainer.asDatabaseModel(): Array<FoodEntity> {
    return foods.map {
        FoodEntity(
            id = it.id,
            name = it.title,
            imgUrl = it.images.get(0)
        )
    }.toTypedArray()
}
