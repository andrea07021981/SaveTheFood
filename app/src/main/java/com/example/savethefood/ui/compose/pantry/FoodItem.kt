package com.example.savethefood.ui.compose.pantry

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.ui.compose.component.SaveTheFoodCard
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import com.example.savethefood.util.getResourceByName

@Composable
fun FoodItem(
    foodDomain: FoodDomain,
    onFoodClick: (FoodDomain) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context =  LocalContext.current
    SaveTheFoodCard(
        modifier = modifier.defaultMinSize(minHeight = 100.dp),
        item = foodDomain,
        onItemClick = onFoodClick
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            val (img, title) = createRefs()
            Image(
                modifier = Modifier
                    .constrainAs(img) {
                        top.linkTo(parent.top, margin = 16.dp)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    },
                painter = painterResource(id = it.img.id.getResourceByName(context = context)),
                contentDescription = null, // decorative element
                contentScale = ContentScale.Crop
            )

            Text(
                modifier = Modifier
                    .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(img.end, margin = 16.dp)
                    bottom.linkTo(img.bottom)
                },
                text = it.title,
            )

        }
        /*Text(
            modifier = Modifier.padding(16.dp),
            text = foodDomain.title,
            style = MaterialTheme.typography.h2
        )*/
    }
}

@Preview
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewFoodItem() {
    SaveTheFoodTheme {
        FoodItem(
            foodDomain = FoodDomain(title = "Food test"),
            onFoodClick = {}
        )
    }
}