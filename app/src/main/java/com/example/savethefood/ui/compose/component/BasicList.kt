package com.example.savethefood.ui.compose.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun <T> BasicList(
    modifier: Modifier = Modifier,
    foods: List<T>?,
    row: @Composable (T) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        items(items = foods ?: listOf()) {
            Column(modifier = Modifier.fillParentMaxWidth()) {
                row(it)
            }
        }
    }
}

