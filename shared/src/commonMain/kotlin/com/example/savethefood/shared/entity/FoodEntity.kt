
package com.example.savethefood.shared.entity

import com.example.savethefood.shared.utils.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FoodEntity(
    @SerialName("id")
    var id: Int = 0,

    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String?,

    @SerialName("img")
    val img: FoodImage,

    @SerialName("price")
    val price: Double?,

    @SerialName("quantity_type")
    val quantityType: QuantityType,

    @SerialName("quantity")
    val quantity: Double?,

    @SerialName("storage")
    val storageType: StorageType,

    @SerialName("best_before")
    var foodBestBefore: Int,

    @SerialName("last_update")
    var lastUpdate: Int
)

/*fun FoodEntity.asDomainModel(): FoodDomain {
    return FoodDomain(
        id = id,
        title = title,
        description = description,
        img = img,
        price = price,
        quantityType = quantityType,
        quantity = quantity,
        storageType = storageType,
        bestBefore = foodBestBefore,
        lastUpdate = lastUpdate
    )
}

fun List<FoodEntity>.asDomainModel(): List<FoodDomain> {
    return map {
        FoodDomain(
            id = it.id,
            title = it.title,
            description = it.description,
            img = it.img,
            price = it.price,
            quantityType = it.quantityType,
            quantity = it.quantity,
            storageType = it.storageType,
            bestBefore = it.foodBestBefore,
            lastUpdate = it.lastUpdate
        )
    }

}

fun Flow<List<FoodEntity>?>.asDomainModel(): Flow<List<FoodDomain>> {
    return map {
        it!!.map {
            FoodDomain(
                id = it.id,
                title = it.title,
                description = it.description,
                img = it.img,
                price = it.price,
                quantityType = it.quantityType,
                quantity = it.quantity,
                storageType = it.storageType,
                bestBefore = it.foodBestBefore,
                lastUpdate = it.lastUpdate
            )
        }
    }
}*/
