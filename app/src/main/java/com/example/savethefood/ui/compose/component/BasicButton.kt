package com.example.savethefood.ui.compose.component

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.savethefood.R
import com.example.savethefood.ui.theme.SaveTheFoodTheme

@Composable
fun BasicButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = SaveTheFoodTheme.colors.brand,
    contentColor: Color = SaveTheFoodTheme.colors.textPrimary,
    context: Context = LocalContext.current,
    @StringRes
    text: Int,
    textStyle: TextStyle = MaterialTheme.typography.button,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ),
        onClick = onClick
    ) {
        Text(
            text = context.resources.getString(text),
            style = textStyle
        )
    }
}