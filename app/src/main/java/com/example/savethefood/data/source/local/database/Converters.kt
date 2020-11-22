package com.example.savethefood.data.source.local.database

import androidx.room.TypeConverter
import com.example.savethefood.util.FoodImage
import com.example.savethefood.util.StorageType

class FoodImageConverter{

    @TypeConverter
    fun fromFoodImage(value: FoodImage): String{
        return value.id
    }

    @TypeConverter
    fun toFoodImage(value: String): FoodImage = when(value){
        "ic_apple_1" -> FoodImage.APPLE
        "ic_apple_1" -> FoodImage.MEAT
        else -> FoodImage.EMPTY
    }
}

class StorageTypeConverter{

    @TypeConverter
    fun fromStorageType(value: StorageType): String {
        return value.type
    }

    @TypeConverter
    fun toStorageType(value: String): StorageType = when(value){
        "Fridge" -> StorageType.FRIDGE
        "Freezer" -> StorageType.FREEZER
        "Dry" -> StorageType.DRY
        else -> StorageType.UNKNOWN
    }
}