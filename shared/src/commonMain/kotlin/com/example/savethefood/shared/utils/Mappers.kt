package com.example.savethefood.shared.utils

import com.example.savethefood.shared.data.source.local.entity.BagEntity
import com.example.savethefood.shared.data.source.local.entity.FoodEntity
import com.example.savethefood.shared.data.source.local.entity.RecipeIngredientEntity
import com.example.savethefood.shared.data.source.local.entity.UserEntity

fun mapToRecipeEntity(
    recipeId: Long = 0,
    id: Long,
    title: String,
    image: String,
    imageType:String,
    likes: Long,
    missedIngredientCount: Long,
    usedIngredientCount: Long,
    unUsedIngredientCount: Long
): RecipeIngredientEntity {
    return RecipeIngredientEntity(
        recipeId = recipeId,
        id = id,
        title = title,
        image = image,
        imageType = imageType,
        likes = likes.toInt(),
        missedIngredientCount = missedIngredientCount.toInt(),
        usedIngredientCount = usedIngredientCount.toInt(),
        unUsedIngredientCount = unUsedIngredientCount.toInt()
    )
}


fun mapToFoodEntity(
    id: Long,
    title: String,
    description: String?,
    img: FoodImage,
    price: Double?,
    quantityType: QuantityType,
    quantity: Double?,
    storageType: StorageType,
    best: Long,
    last: Long
): FoodEntity {
    return FoodEntity(
        id = id,
        title = title,
        description = description,
        img = img,
        price = price,
        quantityType = quantityType,
        quantity = quantity,
        storageType = storageType,
        bestBefore = best,
        lastUpdate = last
    )
}

fun mapToBagEntity(
    id: Long,
    title: String,
    img: FoodImage,
    quantityType: QuantityType,
    quantity: Double?,
): BagEntity {
    return BagEntity(
        id = id.toInt(),
        title = title,
        img = img,
        quantityType = quantityType,
        quantity = quantity
    )
}

fun mapToUserEntity(
    id: Long,
    username: String,
    email: String,
    password: String
): UserEntity {
    return UserEntity(
        id = id,
        userName = username,
        email = email,
        password = password
    )
}