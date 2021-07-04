
package com.example.savethefood.shared.data.source.local.entity

import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.utils.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FoodEntity(
    @SerialName("id")
    var id: Long = 0,

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
    var bestBefore: Long,

    @SerialName("last_update")
    var lastUpdate: Long
)

fun FoodEntity.asDomainModel(): FoodDomain {
    return FoodDomain(
        id = id,
        title = title,
        description = description,
        img = img,
        price = price,
        quantityType = quantityType,
        quantity = quantity,
        storageType = storageType,
        bestBefore = bestBefore,
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
            bestBefore = it.bestBefore,
            lastUpdate = it.lastUpdate
        )
    }

}

fun Flow<List<FoodEntity>?>.asDomainModel(): Flow<List<FoodDomain>> {
    return map {
        it!!.map { entity ->
            FoodDomain(
                id = entity.id,
                title = entity.title,
                description = entity.description,
                img = entity.img,
                price = entity.price,
                quantityType = entity.quantityType,
                quantity = entity.quantity,
                storageType = entity.storageType,
                bestBefore = entity.bestBefore,
                lastUpdate = entity.lastUpdate
            )
        }
    }
}
