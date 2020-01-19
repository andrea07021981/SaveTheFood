package com.example.savethefood.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.R
import com.example.savethefood.component.RecipeAdapter
import com.example.savethefood.constants.ApiCallStatus
import com.example.savethefood.constants.Done
import com.example.savethefood.constants.Loading
import com.example.savethefood.local.domain.RecipeResult
import java.lang.Error


@BindingAdapter("recipeStatus")
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

