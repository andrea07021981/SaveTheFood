package com.example.savethefood.ui.compose.component

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.ui.theme.SaveTheFoodTheme

@Composable
fun <T> BasicCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    color: Color = SaveTheFoodTheme.colors.brandSecondary,
    contentColor: Color = SaveTheFoodTheme.colors.textPrimary,
    border: BorderStroke? = BorderStroke(2.dp, SaveTheFoodTheme.colors.uiBorder),
    elevation: Dp = 16.dp,
    item: T,
    content: @Composable (T) -> Unit
) = Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(22.dp),
                clip = true
            ),
            //.clickable { onItemClick(item) },
        shape = shape,
        backgroundColor = color,
        contentColor = contentColor,
        border = border,
        elevation = elevation,
    ) {
        content(item)
    }

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewBasicCard() {
    SaveTheFoodTheme {
        BasicCard(
            item = FoodDomain(title = "test")
        ) {
            Text(text = it.title, modifier = Modifier.padding(16.dp))
        }
    }
}