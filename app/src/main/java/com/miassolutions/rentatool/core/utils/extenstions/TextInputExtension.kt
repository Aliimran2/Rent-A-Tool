package com.miassolutions.rentatool.core.utils.extenstions

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/*
* This extension sets new text to a TextInputEditText only if it's different from the current text.
* It avoids unnecessary updates and UI refreshes. setSelection(newText.length) moves the cursor to the end.
* Useful in two-way data binding or ViewModel state updates to prevent infinite loops or flickering when text is re-set unnecessarily.
 */

fun TextInputEditText.setTextIfChanged(newText: String) {
    if (text?.toString() != newText) {
        setText(newText)
        setSelection(newText.length)
    }
}

fun TextInputEditText.clearInputs(vararg fields : TextInputEditText){
    fields.forEach { it.setText("") }
}