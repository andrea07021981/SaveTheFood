package com.example.savethefood.util

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.savethefood.R
import com.example.savethefood.constants.QuantityType
import com.example.savethefood.constants.RecipeType
import com.example.savethefood.constants.StorageType
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.home.FoodAdapter
import com.example.savethefood.recipe.RecipeAdapter
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.abs

object HomeBindingUtils {

    /**
     * Binding adapter used to hide the spinner once data is available
     */
    @JvmStatic
    @BindingAdapter("bind:goneIfNotNull")
    fun goneIfNotNull(view: View, it: Result<List<FoodDomain>>?) {
        view.visibility = it?.let { result ->
            if (result is Result.Loading) View.VISIBLE else View.GONE
        } ?: View.VISIBLE
    }

    /**
     * Uses the Glide library to load an image by URL into an [ImageView]
     */
    @JvmStatic
    @BindingAdapter("bind:imageLocalUrl")
    fun ImageView.bindImageLocalUrl(img: FoodImage?) {
        setImageResource(img?.let {
            resources.getIdentifier(img.id, "drawable", context.packageName)
        } ?: R.drawable.ic_broken_image)
    }

    @JvmStatic
    @BindingAdapter("bind:imageUrl")
    fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            Glide.with(imgView.context)
                .load(imgUri)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                )
                .into(imgView)
        }
    }

    @JvmStatic
    @BindingAdapter("bind:listData")
    fun bindFoodRecycleView(recyclerView: RecyclerView, data: List<FoodDomain>?) {
        val adapter = recyclerView.adapter as FoodAdapter
        adapter.submitList(data)
        recyclerView.scheduleLayoutAnimation();
    }

    @JvmStatic
    @BindingAdapter(value = ["listData", "storageType"], requireAll = true)
    fun bindFoodRecycleView(recyclerView: RecyclerView, data: List<FoodDomain>?, storageType: StorageType?) {
        val adapter = recyclerView.adapter as FoodAdapter
        adapter.submitList(
            if (storageType == StorageType.ALL) {
                data
            } else {
                data?.filter { foodDomain -> foodDomain.storageType == storageType }
            })
        recyclerView.scheduleLayoutAnimation();
    }

    // TODO FInd a unique way with above method
    @JvmStatic
    @BindingAdapter(value = ["listData", "recipeType"], requireAll = true)
    fun bindRecipeRecycleView(recyclerView: RecyclerView, data: List<RecipeResult>?, storageType: RecipeType?) {
        val adapter = recyclerView.adapter as RecipeAdapter
        adapter.submitList(
            data?.filter {
                if (storageType == RecipeType.REMOTE) {
                    it.recipeId == 0L
                } else {
                    it.recipeId != 0L
                }
            }
        )
        recyclerView.scheduleLayoutAnimation();
    }

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter("bind:formatExpireDate")
    fun TextView.bindFormatDate(date: Date) {
        val currentDate = LocalDate.now()
        val oldDate = date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        val diff = ChronoUnit.DAYS.between(currentDate, oldDate)

        when {
            diff in 0..3 -> {
                text = context.getString(R.string.expiring_in_date, diff)
                setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_light))
            }
            diff < 0 -> {
                text = context.getString(R.string.expired_from, abs(diff))
                setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light))
            }
            else -> {
                text = context.getString(R.string.days_in, diff)
                setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["bind:unit", "bind:formatQuantity"], requireAll = true)
    fun TextView.bindFormatQuantity(unit: QuantityType, data: Double) {
        text = if (unit == QuantityType.UNIT) {
            context.resources.getQuantityString(R.plurals.units, data.toInt(), data.toInt())
        } else {
            if (data < 1) {
                context.getString(R.string.gr, data)
            } else {
                context.getString(R.string.kg, data)
            }
        }
    }
}
