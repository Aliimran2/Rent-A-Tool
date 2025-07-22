package com.miassolutions.rentatool.core.utils.helper

import android.view.View
import com.google.android.material.textfield.TextInputEditText


fun clearInputs(vararg inputFields : TextInputEditText){
    inputFields.forEach { field ->
        field.text?.clear()
    }
}


fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}
