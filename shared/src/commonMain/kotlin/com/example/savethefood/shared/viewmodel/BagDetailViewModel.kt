package com.example.savethefood.shared.viewmodel

import com.example.savethefood.shared.data.source.repository.FoodRepository
import com.example.savethefood.shared.data.source.repository.ShoppingRepository


expect class BagDetailViewModel(
    shoppingDataRepository: ShoppingRepository,
    foodDataRepository: FoodRepository,
)