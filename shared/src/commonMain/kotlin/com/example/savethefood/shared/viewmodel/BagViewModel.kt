package com.example.savethefood.shared.viewmodel

import com.example.savethefood.shared.data.domain.BagDomain
import com.example.savethefood.shared.data.source.repository.ShoppingRepository
import com.example.savethefood.shared.utils.Event
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform


expect class BagViewModel (
    shoppingDataRepository: ShoppingRepository,
)