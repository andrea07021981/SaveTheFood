package com.example.savethefood.ui.compose.pantry

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.ui.compose.component.FoodExpiringStatusText
import com.example.savethefood.ui.compose.extention.formatQuantityByType
import com.example.savethefood.util.getResourceByName

@Composable
internal fun FoodItem(
    modifier: Modifier = Modifier,
    food: FoodDomain
) {
    val context =  LocalContext.current
    ConstraintLayout(
        modifier = modifier
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
            painter = painterResource(id = food.img.id.getResourceByName(context = context)),
            contentDescription = null, // decorative element
            contentScale = ContentScale.Crop
        )

        Row(modifier = Modifier
            .constrainAs(firstLine) {
                top.linkTo(img.top)
                start.linkTo(img.end, margin = 16.dp)
                bottom.linkTo(img.bottom)
                end.linkTo(parent.end, margin = 0.dp)
                width =
                    Dimension.fillToConstraints // Significant to keep the row inside constraints (Equivalent of constraint layout 0dp)
            }
        ) {
            Text(
                modifier = Modifier.weight(1F),
                style = MaterialTheme.typography.h6,
                text = food.title,
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier.weight(1F),
                style = MaterialTheme.typography.h6,
                text = food.formatQuantityByType(context),
                textAlign = TextAlign.End
            )
        }

        Row(modifier = Modifier
            .constrainAs(secondLine) {
                top.linkTo(firstLine.bottom, margin = 16.dp)
                start.linkTo(firstLine.start, margin = 0.dp)
                bottom.linkTo(parent.bottom, margin = 16.dp)
                end.linkTo(parent.end, margin = 0.dp)
                width =
                    Dimension.fillToConstraints // Significant to keep the row inside constraints (Equivalent of constraint layout 0dp)
            }
        ) {
            FoodExpiringStatusText(
                modifier = Modifier.weight(1F),
                textAlign = TextAlign.Start,
                foodDomain = food
            )
            Text(
                modifier = Modifier.weight(.3F),
                text = food.storageType.type,
                textAlign = TextAlign.End
            )
        }
    }
}