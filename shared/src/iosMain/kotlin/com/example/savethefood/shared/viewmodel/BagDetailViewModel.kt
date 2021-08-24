package com.example.savethefood.shared.viewmodel

import com.example.savethefood.shared.data.source.repository.FoodRepository
import com.example.savethefood.shared.data.source.repository.RecipeRepository
import com.example.savethefood.shared.data.source.repository.ShoppingRepository

actual class BagDetailViewModel actual constructor(
    shoppingDataRepository: ShoppingRepository,
    foodDataRepository: FoodRepository,
)