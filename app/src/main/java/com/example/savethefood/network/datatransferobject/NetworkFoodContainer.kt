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
/*WE'LL USE IT ONLY IN CASE OF JSON ARRAY*/
/*@JsonClass(generateAdapter = true)
data class NetworkFoodContainer(val foods: List<NetworkFood>)

*//**
 * Foods represent a devbyte that can be played.
 *//*
@JsonClass(generateAdapter = true)
data class NetworkFood(
    val id: Int,
    val title: String)

*//**
 * Convert Network results to database objects
 *//*
fun NetworkFoodContainer.asDomainModel(): Food {
    return Food(
            foodId = foods.id,
            foodName = foods.title,
            foodImgUrl = foods.title)
}

fun NetworkFoodContainer.asDatabaseModel(): FoodEntity {
    return FoodEntity(
            id = foods.id,
            name = foods.title,
            imgUrl = foods.title
        )
}*/
