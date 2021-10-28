package com.example.savethefood.ui.compose.pantry

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.ui.compose.component.SaveTheFoodCard
import com.example.savethefood.ui.theme.SaveTheFoodTheme

@Composable
fun FoodItem(
    foodDomain: FoodDomain,
    onFoodClick: (FoodDomain) -> Unit,
    modifier: Modifier = Modifier,
) {
    SaveTheFoodCard(
        modifier = modifier,
        item = foodDomain,
        onItemClick = onFoodClick
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = foodDomain.title,
            style = MaterialTheme.typography.h2
        )
    }
}

@Preview
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewFoodItem() {
    SaveTheFoodTheme {
        FoodItem(
            foodDomain = FoodDomain(title = "Food test"),
            onFoodClick = {}
        )
    }
}