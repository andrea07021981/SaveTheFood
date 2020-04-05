package com.example.savethefood.util

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.savethefood.R
import com.example.savethefood.component.RecipeAdapter
import com.example.savethefood.constants.ApiCallStatus
import com.example.savethefood.constants.Done
import com.example.savethefood.constants.Loading
import com.example.savethefood.local.domain.RecipeInfoDomain
import com.example.savethefood.local.domain.RecipeResult
import kotlin.math.min


@BindingAdapter("recipeApiStatus")
fun bindStatus(statusImageView: ImageView, status: ApiCallStatus) {
    when (status){
        is Loading -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        is Error -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        is Done -> {
            statusImageView.visibility = View.GONE
        }
    }
}


@BindingAdapter("listdata")
fun bindRecycleView(recyclerView: RecyclerView, data: List<RecipeResult>?) {
    val adapter = recyclerView.adapter as RecipeAdapter
    adapter.submitList(data)
}

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageRecipeUrl")
fun bindRecipeImage(imgView: ImageView, recipeResult: RecipeResult?) {
    recipeResult?.let {
        val imgUri = recipeResult
            .baseDomainUrl
            .plus(recipeResult.image)
            .toUri()
            .buildUpon()
            .scheme("https")
            .build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}

/**
 *  set the starts 0 out of 100
 */
@BindingAdapter("starsValue")
fun AppCompatRatingBar.starsValue(recipe: RecipeInfoDomain?) {
    numStars = when(recipe?.recipeSpoonacularScore?.toInt()) {
        in 0..20 -> 1
        in 21..40 -> 2
        in 41..60 -> 3
        in 61..80 -> 4
        in 81..100 ->5
        else -> 0
    }
}

/**
 *  set the health 0 out of 100
 */
@BindingAdapter("healthValue")
fun TextView.healthValue(recipe: RecipeInfoDomain?) {
    text = when(recipe?.recipeHealthScore?.toInt()) {
        in 0..20 -> "POOR"
        in 21..40 -> "AVERAGE"
        in 41..60 -> "GOOD"
        in 61..80 -> "VERY GOOD"
        in 81..100 -> "EXCELLENT"
        else -> "NONE"
    }
}

/**
 *  Format the minutes
 */
@BindingAdapter("formattedText")
fun TextView.formattedText(minutes: Int?) {
    text = minutes?.let {
        String.format(context.getString(
            R.string.format__date,
            it.div(60),
            it.rem(60)))
    }
}