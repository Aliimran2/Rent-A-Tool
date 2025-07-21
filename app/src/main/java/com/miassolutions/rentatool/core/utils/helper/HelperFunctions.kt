package com.miassolutions.rentatool.core.utils.helper

import com.google.android.material.textfield.TextInputEditText


fun clearInputs(vararg inputFields : TextInputEditText){
    inputFields.forEach { field ->
        field.text?.clear()
    }
}

