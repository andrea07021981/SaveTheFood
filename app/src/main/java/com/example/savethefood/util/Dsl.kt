package com.example.savethefood.util

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog

/**
 * Domain-Specific language for building a dialog
 * If we need another param, ad example an Int, we can use AlertDialog.Builder.(Int) and use it with it
 *
 * This fun uses the additional receiver which makes easier the build with this. We can also use
 * the high order without receiver, but we need to access the properties with it
 * 
 * inline fun buildDialog(
    context: Context,
    buildDialog: (AlertDialog.Builder) -> Unit
    ): AlertDialog {
        val builder = AlertDialog.Builder(context)
        buildDialog(builder)
        return builder.create()
    }
 */
inline fun buildDialog(
    context: Context,
    buildDialog: AlertDialog.Builder.() -> Unit
): AlertDialog {
    val builder = AlertDialog.Builder(context)
    builder.buildDialog()
    return builder.create()
}

fun AlertDialog.Builder.title(title: String) {
    setTitle(title)
}

fun AlertDialog.Builder.message(title: String) {
    setMessage(title)
}

fun AlertDialog.Builder.negativeButton(
    negativeText: String = "CANCEL",
    action: ((DialogInterface, Int) -> Unit)? = null
) {
    setNegativeButton(negativeText) { dialog, which -> action?.invoke(dialog, which) }
}

fun AlertDialog.Builder.positiveButton(
    positiveText: String = "OK",
    action: ((DialogInterface, Int) -> Unit)? = null
) {
    setPositiveButton(positiveText) { dialog, which -> action?.invoke(dialog, which) }
}

fun AlertDialog.Builder.addCustomView(
    view: View
) {
    setView(view)
}
