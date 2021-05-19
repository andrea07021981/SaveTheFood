package com.example.savethefood.util

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog

/**
 * Domain-Specific language for building a dialog
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
