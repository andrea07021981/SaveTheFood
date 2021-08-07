package com.example.savethefood.shared.data.domain

import com.example.savethefood.shared.Parcelable
import com.example.savethefood.shared.Parcelize
import com.example.savethefood.shared.data.source.local.entity.FoodEntity
import com.example.savethefood.shared.utils.FoodImage
import com.example.savethefood.shared.utils.QuantityType
import com.example.savethefood.shared.utils.StorageType
import io.ktor.util.date.*
import kotlinx.serialization.Serializable

//Food
@Serializable
@Parcelize
data class FoodDomain(
    var title: String = "",
    var description: String? = "",
    var id: Long = 0L,
    var img: FoodImage = FoodImage.EMPTY,
    var price: Double? = null,
    var quantityType: QuantityType = QuantityType.UNIT,
    var quantity: Double? = null,
    var storageType: StorageType = StorageType.FRIDGE,
    var bestBefore: Long? = GMTDate().timestamp,
    var lastUpdate: Long? = GMTDate().timestamp,
) : Parcelable

fun FoodDomain.asDatabaseModel(): FoodEntity {
    return FoodEntity(
        id = id,
        title = title,
        description = description,
        img = img,
        price = price,
        quantityType = quantityType,
        quantity = quantity,
        storageType = storageType,
        bestBefore = bestBefore ?: GMTDate().timestamp,
        lastUpdate = lastUpdate ?: GMTDate().timestamp
    )
}

// TODO use DSL for domains and other classes like
// https://proandroiddev.com/writing-dsls-in-kotlin-part-1-7f5d2193f277

fun food(block: (FoodDomain) -> Unit): FoodDomain {
    val p = FoodDomain()
    block(p)
    return p
}