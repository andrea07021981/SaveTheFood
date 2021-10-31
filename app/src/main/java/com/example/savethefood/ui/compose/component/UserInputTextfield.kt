package com.example.savethefood.ui.compose.component

import android.content.res.Configuration
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.savethefood.R
import com.example.savethefood.ui.theme.AlphaHalf
import com.example.savethefood.ui.theme.SaveTheFoodTheme

@Composable
fun UserInputTextfield(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChanged,
        label = { Text("User") },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.email_white_24dp),
                contentDescription = null,// decorative element
                tint = SaveTheFoodTheme.colors.brand
            )
        },
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = SaveTheFoodTheme.colors.brand,
            focusedBorderColor = SaveTheFoodTheme.colors.brand,
            unfocusedBorderColor = SaveTheFoodTheme.colors.brand.copy(alpha = AlphaHalf),
            focusedLabelColor = SaveTheFoodTheme.colors.brand,
            unfocusedLabelColor = SaveTheFoodTheme.colors.brand,
            backgroundColor = Color.Transparent,
            cursorColor = SaveTheFoodTheme.colors.textLink,
            disabledLabelColor = SaveTheFoodTheme.colors.textLink
        ),
    )
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewUserInputTextfield() {
    SaveTheFoodTheme {
        UserInputTextfield(
            text = "Preview",
            onTextChanged = {}
        )
    }
}