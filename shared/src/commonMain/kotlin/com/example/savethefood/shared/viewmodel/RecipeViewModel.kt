package com.example.savethefood.shared.viewmodel

import com.example.savethefood.shared.data.source.repository.RecipeRepository

expect class RecipeViewModel(
    recipeDataRepository: RecipeRepository,
)