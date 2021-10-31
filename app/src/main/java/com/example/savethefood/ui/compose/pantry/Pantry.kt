package com.example.savethefood.ui.compose.pantry

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.savethefood.shared.viewmodel.HomeViewModel
import com.example.savethefood.ui.compose.SaveTheFoodApp
import com.example.savethefood.ui.compose.foodList
import org.koin.androidx.compose.getViewModel


@Composable
fun Pantry(
    modifier: Modifier = Modifier,
    onFoodSelected: (Long) -> Unit,
    viewModel: HomeViewModel = getViewModel() // Koin
) {
    val foods by viewModel.foodList.observeAsState()
    // TODO pass the filters jetsnak
    FoodList(modifier, foods, onFoodSelected)
}


@Preview
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewPantryScreen() {
    SaveTheFoodApp {
        FoodList(
            foods = foodList,
            onFoodSelected = {},
        )
    }
}