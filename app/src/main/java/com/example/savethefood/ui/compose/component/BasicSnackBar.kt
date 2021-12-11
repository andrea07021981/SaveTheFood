package com.example.savethefood.ui.compose.component

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.savethefood.ui.theme.SaveTheFoodTheme

/**
 * Custom snack bar using the save the food colors
 */
@Composable
fun BasicSnackBar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
    actionOnNewLine: Boolean = false,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = SaveTheFoodTheme.colors.warning,
    contentColor: Color = SaveTheFoodTheme.colors.textPrimary,
    actionColor: Color = SaveTheFoodTheme.colors.brand,
    elevation: Dp = 6.dp
) {
    Snackbar(
        snackbarData = snackbarData,
        modifier = modifier,
        actionOnNewLine = actionOnNewLine,
        shape = shape,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        actionColor = actionColor,
        elevation = elevation
    )
}
