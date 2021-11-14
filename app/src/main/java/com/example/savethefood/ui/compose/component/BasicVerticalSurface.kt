package com.example.savethefood.ui.compose.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import com.google.accompanist.insets.imePadding

@Composable
fun BasicVerticalSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    background: Color = SaveTheFoodTheme.colors.uiBackground,
    contentColor: Color = SaveTheFoodTheme.colors.textSecondary,
    elevation: Dp = 20.dp,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Surface(
        modifier = modifier.fillMaxWidth()
            .fillMaxHeight()
            .imePadding(), // This move the view up while the keyboard is opened,
        shape = shape,
        color = background,
        contentColor = contentColor,
        elevation = elevation,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}