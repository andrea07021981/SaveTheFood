package com.example.savethefood.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import com.google.accompanist.insets.ProvideWindowInsets

@Composable
fun SaveTheFoodApp(content: @Composable () -> Unit) {
    ProvideWindowInsets {
        SaveTheFoodTheme {
            content()
        }
    }
}

@Composable
fun MainApp() {
    // A surface container using the 'color' color from the theme
    Surface(
        contentColor = SaveTheFoodTheme.colors.textPrimary
    ) {
        SaveTheFoodScaffold(
            contentColor = SaveTheFoodTheme.colors.textPrimary
        ) { innerPaddingModifier ->
            Column(
                modifier = Modifier.padding(innerPaddingModifier)
            ) {
                Greeting(name = "Andrea")
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(
        text = "Hello $name!")
}