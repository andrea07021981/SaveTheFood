package com.example.savethefood.ui.compose.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.ui.compose.extention.bindExpireDate
import com.example.savethefood.ui.theme.SaveTheFoodTheme

@Composable
fun FoodExpiringStatusText(
    modifier: Modifier = Modifier,
    textAlign: TextAlign?,
    foodDomain: FoodDomain
) {
    val (text, color) = foodDomain.bestBefore.bindExpireDate(LocalContext.current)
    Text(
        modifier = modifier,
        textAlign = textAlign,
        text = text,
        color = color
    )
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewFoodExpiringStatusText() {
    SaveTheFoodTheme {
        BasicCard(
            item = FoodDomain(title = "test")
        ) {
            Text(text = it.title, modifier = Modifier.padding(16.dp))
        }
    }
}