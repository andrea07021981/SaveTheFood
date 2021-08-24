package com.example.savethefood.shared.viewmodel

import com.example.savethefood.shared.data.source.repository.FoodRepository

expect class HomeViewModel(
    foodDataRepository: FoodRepository
)