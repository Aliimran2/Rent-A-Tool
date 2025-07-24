package com.miassolutions.rentatool.utils.extenstions

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
    actionText: String? = null,
    actionListener: View.OnClickListener? = null
) {
    val snackbar = Snackbar.make(this, message, duration)

    actionText?.let {
        snackbar.setAction(it, actionListener)
    }

    snackbar.show()
}