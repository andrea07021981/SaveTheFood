package com.example.savethefood.util

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.savethefood.R
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.ProductDomain
import com.example.savethefood.food.FoodSearchAdapter

/**
 * Needs to be used with [NumberOfSetsConverters.setArrayToString].
 */
@BindingAdapter("bind:numberOfSets")
fun setNumberOfSets(view: EditText, value: String) {
    view.setText(value)
}

@BindingAdapter("bind:listFoods")
fun bindRecycleView(recyclerView: RecyclerView, data: List<ProductDomain>?) {
    val adapter = recyclerView.adapter as FoodSearchAdapter
    adapter.submitList(data)
}

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("bind:imageFoodUrl")
fun bindFoodImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}

@BindingAdapter("bind:htmlConverter")
fun TextView.bindFoodDescription(html: String?) {//TODO check if we can directly bind in xml
    html?.let { text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY); }
}