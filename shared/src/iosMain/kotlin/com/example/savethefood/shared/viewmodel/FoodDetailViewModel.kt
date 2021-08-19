package com.example.savethefood.shared.viewmodel

import com.example.savethefood.shared.data.source.repository.FoodRepository
import com.example.savethefood.shared.data.source.repository.RecipeRepository

actual class FoodDetailViewModel actual constructor(
    private val foodDataRepository: FoodRepository,
    private val recipeDataRepository: RecipeRepository,
) 