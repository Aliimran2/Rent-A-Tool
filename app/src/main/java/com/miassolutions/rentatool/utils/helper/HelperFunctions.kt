package com.miassolutions.rentatool.utils.helper

import android.content.Context
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText


fun clearInputs(vararg inputFields : TextInputEditText){
    inputFields.forEach { field ->
        field.text?.clear()
    }
}


fun showToast(context: Context, message : String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


