package com.example.savethefood.ui.compose.extention

import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.savethefood.R
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.abs

@Composable
fun Long?.bindExpireDate(context: Context): Pair<String, Color> {
    return this?.let {
        val diff = getDiff(this)

        when {
            diff in 0..3 -> {
                context.getString(R.string.expiring_in_date, diff) to
                        SaveTheFoodTheme.colors.warning
            }
            diff < 0 -> {
                context.getString(R.string.expired_from, abs(diff)) to
                        SaveTheFoodTheme.colors.error
            }
            else -> {
                context.getString(R.string.days_in, diff) to
                        SaveTheFoodTheme.colors.uiBorder
            }
        }
    } ?: "--" to SaveTheFoodTheme.colors.uiBorder
}

fun getDiff(foodDate: Long): Long {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val currentDate = LocalDate.now()
        val oldDate = Date(foodDate).toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        ChronoUnit.DAYS.between(currentDate, oldDate)
    } else {
        val today = Calendar.getInstance().time
        (today.time - foodDate) / (24 * 60 * 60 * 1000)
    }
}
