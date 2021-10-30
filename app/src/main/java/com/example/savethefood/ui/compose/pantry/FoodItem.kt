package com.example.savethefood.ui.compose.pantry

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.ui.compose.component.SaveTheFoodCard
import com.example.savethefood.ui.compose.extention.bindExpireDate
import com.example.savethefood.ui.compose.extention.formatQuantityByType
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
        modifier = modifier,
        item = foodDomain,
        onItemClick = onFoodClick
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            val (img, firstLine, secondLine) = createRefs()
            Image(
                modifier = Modifier
                    .constrainAs(img) {
                        top.linkTo(parent.top, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    },
                painter = painterResource(id = it.img.id.getResourceByName(context = context)),
                contentDescription = null, // decorative element
                contentScale = ContentScale.Crop
            )

            Row(modifier = Modifier
                .constrainAs(firstLine) {
                    top.linkTo(img.top)
                    start.linkTo(img.end, margin = 16.dp)
                    bottom.linkTo(img.bottom)
                    end.linkTo(parent.end, margin = 0.dp)
                    width = Dimension.fillToConstraints // Significant to keep the row inside constraints (Equivalent of constraint layout 0dp)
                }
            ) {
                Text(
                    modifier = Modifier.weight(1F),
                    style = MaterialTheme.typography.h6,
                    text = it.title,
                    textAlign = TextAlign.Start
                )
                Text(
                    modifier = Modifier.weight(1F),
                    style = MaterialTheme.typography.h6,
                    text = it.formatQuantityByType(context),
                    textAlign = TextAlign.End
                )
            }

            Row(modifier = Modifier
                .constrainAs(secondLine) {
                    top.linkTo(firstLine.bottom, margin = 16.dp)
                    start.linkTo(firstLine.start, margin = 0.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                    width = Dimension.fillToConstraints // Significant to keep the row inside constraints (Equivalent of constraint layout 0dp)
                }
            ) {
                FoodStatusText(
                    modifier = Modifier.weight(1F),
                    textAlign = TextAlign.Start,
                    foodDomain = it
                )
                Text(
                    modifier = Modifier.weight(.3F),
                    text = it.storageType.type,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
fun FoodStatusText(
    modifier: Modifier = Modifier,
    textAlign: TextAlign?,
    foodDomain: FoodDomain
) {
    val (text, color) = foodDomain.bestBefore.bindExpireDate(LocalContext.current)
    Text(
        modifier = modifier,
        textAlign = textAlign,
        text = text,
        color = color
    )
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