package com.example.savethefood.shared.viewmodel

import com.example.savethefood.shared.data.source.repository.FoodRepository
import com.example.savethefood.shared.data.source.repository.RecipeRepository

actual class HomeViewModel actual constructor(
    private val foodDataRepository: FoodRepository
)