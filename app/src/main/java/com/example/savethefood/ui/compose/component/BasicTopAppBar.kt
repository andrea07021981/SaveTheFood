package com.example.savethefood.ui.compose.component

import android.text.style.BackgroundColorSpan
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.savethefood.ui.theme.SaveTheFoodTheme

/**
 * Basic top app bar. For now do not need the statusBarsPadding() and
 * navigationBarsPadding(bottom = false) because the root has the padding for the status bar height
 */
@Composable
fun BasicTopAppBar(
    modifier: Modifier = Modifier,
    contentColor: Color = SaveTheFoodTheme.colors.textPrimary,
    backgroundColor: Color = SaveTheFoodTheme.colors.uiBackground,
    elevation: Dp = 0.dp,
    title: @Composable () -> Unit,
    homeButton: @Composable () -> Unit,
    actions: @Composable() (RowScope.() -> Unit)
) {
    Surface(
        modifier = modifier,
        contentColor = contentColor,
        color = backgroundColor,
        elevation = elevation,
    ) {
        TopAppBar(
            title = title,
            navigationIcon = homeButton,
            actions = actions,
            backgroundColor = Color.Transparent,
            contentColor = contentColorFor(backgroundColor),
            elevation = 0.dp
        )
    }
}