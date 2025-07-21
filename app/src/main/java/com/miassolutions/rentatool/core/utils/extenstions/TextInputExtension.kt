package com.miassolutions.rentatool.core.utils.extenstions

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun TextInputEditText.setTextIfChanged(newText: String) {
    if (text?.toString() != newText) {
        setText(newText)
        setSelection(newText.length)
    }
}

fun TextInputEditText.clearInputs(vararg fields : TextInputEditText){
    fields.forEach { it.setText("") }
}