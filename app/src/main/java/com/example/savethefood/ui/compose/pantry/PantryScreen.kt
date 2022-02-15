package com.example.savethefood.ui.compose.pantry

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.savethefood.shared.viewmodel.HomeViewModel
import com.example.savethefood.ui.compose.SaveTheFoodApp
import com.example.savethefood.ui.compose.component.BasicList
import com.example.savethefood.ui.compose.foodList
import org.koin.androidx.compose.getViewModel

/**
 * This is the main screen where we build all the main components
 * TODO like in Jetcaster app, use combine (see JJetcaster homeviewmodel) for the recipes.
 */
@Composable
fun PantryScreen(
    modifier: Modifier = Modifier,
    onFoodSelected: (Long) -> Unit,
    viewModel: HomeViewModel = getViewModel() // Koin
) {
    val foods by viewModel.foodList.observeAsState()
    // TODO pass the filters jetsnak
    // TODO add here all the Slots api for toolbar, filters, etc. User Scaffold wito top bar!
    BasicList(modifier, foods) {
        FoodCard(
            foodDomain = it,
            onFoodClick = onFoodSelected
        )
    }
}


@Preview
@Preview("Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewPantryScreen() {
    SaveTheFoodApp {
        BasicList(
            items = foodList
        ) {
            FoodCard(
                foodDomain = it,
                onFoodClick = {  }
            )
        }
    }
}