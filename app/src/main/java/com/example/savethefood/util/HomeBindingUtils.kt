package com.example.savethefood.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
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
@BindingAdapter("bind:imageLocalUrl")
fun ImageView.bindImageLocalUrl(img: FoodImage?) {
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

@BindingAdapter("bind:listData")
fun bindFoodRecycleView(recyclerView: RecyclerView, data: List<FoodDomain>?) {
    val adapter = recyclerView.adapter as FoodAdapter
    adapter.submitList(data)
    recyclerView.scheduleLayoutAnimation();
}

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

@BindingAdapter("bind:formatDate")
fun TextView.bindFormatDate(date: Date) {
    //TODO add formatter, change also color use <xliff:
    text = "11 days is"
}

@BindingAdapter("bind:formatQuantity")
fun TextView.bindFormatQuantity(date: Double) {
    //TODO add formatter for grams and kg
    text = "500 g"
}