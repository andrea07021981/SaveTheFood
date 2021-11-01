package com.example.savethefood.ui.compose.pantry

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.ui.compose.component.BasicCard
import com.example.savethefood.ui.compose.extention.bindExpireDate
import com.example.savethefood.ui.compose.extention.formatQuantityByType
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import com.example.savethefood.util.getResourceByName

@Composable
fun FoodCard(
    foodDomain: FoodDomain,
    onFoodClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicCard(
        modifier = modifier,
        item = foodDomain
    ) { food ->
        FoodItem(
            modifier = Modifier.clickable { onFoodClick(food.id) },
            food = food
        )
    }
}

@Preview
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewFoodItem() {
    SaveTheFoodTheme {
        FoodCard(
            foodDomain = FoodDomain(title = "Food test"),
            onFoodClick = {}
        )
    }
}