package com.example.savethefood.util

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.savethefood.R
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.home.FoodAdapter
import java.util.*

/**
 * Binding adapter used to hide the spinner once data is available
 */
@BindingAdapter("bind:goneIfNotNull")
fun goneIfNotNull(view: View, it: Result<List<FoodDomain>>?) {
    view.visibility = it?.let { result ->
        if (result is Result.Loading) View.VISIBLE else View.GONE
    } ?: View.VISIBLE
}

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("bind:imageUrl")
fun ImageView.bindImage(img: FoodImage?) {
    setImageResource(img?.let {
            resources.getIdentifier(img.id, "drawable", context.packageName)
    } ?: R.drawable.ic_broken_image)
}

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

@BindingAdapter("bind:listdata")
fun bindFoodRecycleView(recyclerView: RecyclerView, data: Result<List<FoodDomain>>?) {
    val adapter = recyclerView.adapter as FoodAdapter
    when (data) {
        is Result.Success -> adapter.submitList(data.data)
        else -> adapter.submitList(listOf())
    }
    recyclerView.scheduleLayoutAnimation();
}

@BindingAdapter("bind:formatDate")
fun TextView.bindFormatDate(date: Date) {
    //TODO add formatter, change also color
    text = "11 days is"
}

@BindingAdapter("bind:formatQuantity")
fun TextView.bindFormatQuantity(date: Double) {
    //TODO add formatter for grams and kg
    text = "500 g"
}