package com.example.savethefood.ui.compose.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.savethefood.ui.compose.component.UserInputTextfield
import com.example.savethefood.ui.theme.SaveTheFoodTheme

@Composable
fun Login(
    modifier: Modifier = Modifier
) {
    // TODO test, remove it and use the Viewmodel
    val (name, setName) = rememberSaveable {
        mutableStateOf("")
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        shape = RectangleShape,
        color = SaveTheFoodTheme.colors.uiBackground,
        contentColor = SaveTheFoodTheme.colors.textSecondary,
        elevation = 20.dp,
    ) {
        Column {
            UserInputTextfield(
                text = name,
                onTextChanged = setName
            )
        }
    }
}