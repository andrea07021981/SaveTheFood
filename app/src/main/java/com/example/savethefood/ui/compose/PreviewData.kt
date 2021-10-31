package com.example.savethefood.ui.compose

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.utils.FoodImage
import com.example.savethefood.shared.utils.QuantityType
import com.example.savethefood.shared.utils.StorageType

val foodList =
    listOf(
        FoodDomain(
            title = "Test 1",
            img = FoodImage.ASPARAGUS,
            storageType = StorageType.FRIDGE,
            bestBefore = 1635542008818,
            quantityType = QuantityType.UNIT,
            quantity = 2.0
        ),
        FoodDomain(
            title = "Test 2",
            img = FoodImage.BANANA,
            storageType = StorageType.DRY,
            bestBefore = 1634788800000,
            quantityType = QuantityType.UNIT,
            quantity = 12.0
        ),
        FoodDomain(
            title = "Test 3",
            img = FoodImage.BEER,
            storageType = StorageType.FREEZER,
            bestBefore = 1637470800000,
            quantityType = QuantityType.WEIGHT,
            quantity = 4.5
        ),
        FoodDomain(
            title = "Test 4",
            img = FoodImage.HAMBURGER,
            storageType = StorageType.FREEZER,
            bestBefore = 1632196800000,
            quantityType = QuantityType.WEIGHT,
            quantity = 0.5
        )
    )