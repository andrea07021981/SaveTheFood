package com.example.savethefood.shared.viewmodel

import com.example.savethefood.shared.data.source.repository.FoodRepository
import com.example.savethefood.shared.data.source.repository.RecipeRepository

expect class FoodDetailViewModel(
    foodDataRepository: FoodRepository,
    recipeDataRepository: RecipeRepository,
)