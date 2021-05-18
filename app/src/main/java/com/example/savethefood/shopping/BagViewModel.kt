package com.example.savethefood.shopping

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.savethefood.data.source.repository.ShoppingRepository
import javax.inject.Inject


class BagViewModel @ViewModelInject constructor(
    private val shoppingRepository: ShoppingRepository
) : ViewModel(){
}