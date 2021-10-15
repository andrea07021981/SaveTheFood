package com.example.savethefood.ui.compose

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
        contentColor = SaveTheFoodTheme.colors.textPrimary,
        color = SaveTheFoodTheme.colors.uiBackground
    ) {
        var showSplashScreen by remember {
            mutableStateOf(true)
        }

        // Show the splash screen first
        Crossfade(
            targetState = showSplashScreen,
            animationSpec = tween(durationMillis = 3000, easing = FastOutSlowInEasing)
        ) {
            if (it) {
                SplashScreen(onTimeOut = { showSplashScreen = false })
            } else {
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
    }
}

@Composable
fun Greeting(name: String) {
    Text(
        text = "Hello $name!")
}