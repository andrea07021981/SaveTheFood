package com.example.savethefood.ui.compose.pantry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.savethefood.shared.data.domain.FoodDomain

@Composable
internal fun FoodList(
    modifier: Modifier = Modifier,
    foods: List<FoodDomain>?,
    onFoodSelected: (Long) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
    )
    {
        items(items = foods ?: listOf()) {
            Column(modifier = Modifier.fillParentMaxWidth()) {
                FoodItem(
                    foodDomain = it,
                    onFoodClick = { onFoodSelected(it.id) })
            }
        }
    }
}