package com.example.savethefood.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.text.Layout
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.navigation.fragment.findNavController
import br.com.simplepass.loadingbutton.animatedDrawables.ProgressType
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import br.com.simplepass.loadingbutton.customViews.ProgressButton
import com.example.savethefood.R
import com.example.savethefood.constants.*
import com.example.savethefood.login.LoginFragmentDirections
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("loadViewAnimation")
fun View.animation(@AnimRes resource: Int) {
    val animation = AnimationUtils.loadAnimation(context, resource)
    startAnimation(animation)
}

@BindingAdapter("hasError")
fun TextInputLayout.hasError(error: Boolean) {
    when (error) {
        true -> setError("Mandatory field")
        else -> setError(null)
    }
}

/**
 * This bindiadapter display the login status using [LoginAuthenticationStates]
 */
@BindingAdapter("loginStatus")
fun bindStatus(context: View, status: LoginAuthenticationStates?) {
    when (status) {
        is Authenticated -> {
            Toast.makeText(context.context, "Logged", Toast.LENGTH_SHORT).show()
        }
        is Unauthenticated -> {
            Toast.makeText(context.context, "Not logged", Toast.LENGTH_SHORT).show()
        }
        is InvalidAuthentication -> {
            Toast.makeText(context.context, "Error Login", Toast.LENGTH_SHORT).show()
        }
    }
}


fun CircularProgressButton.morphDoneAndRevert(
    context: Context,
    state: LoginAuthenticationStates?,
    fillColor: Int = ContextCompat.getColor(context, R.color.customGreen),
    bitmap: Bitmap = defaultDoneImage(context.resources),
    doneTime: Long = 3000,
    navigateTime: Long = 1000,
    revertTime: Long = 4000,
    resources: Resources
): Boolean {
    progressType = ProgressType.INDETERMINATE

    when (state) {
        is Authenticating -> {
            startAnimation()
        }
        is Authenticated -> {
            Handler().run {
                return true
            }
        }
        is Unauthenticated -> {
            Handler().run {
                postDelayed(::revertAnimation, revertTime)
            }
        }
        is InvalidAuthentication -> {
            val fillColorError = ContextCompat.getColor(context, R.color.customRed)
            val bitmapError: Bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_check_ko)
            Handler().run {
                postDelayed({ doneLoadingAnimation(fillColorError, bitmapError) }, doneTime)
                postDelayed(::revertAnimation, revertTime)
            }
        }
    }
    return false
}

private fun defaultDoneImage(resources: Resources) =
    BitmapFactory.decodeResource(resources, R.mipmap.ic_check_ok)

