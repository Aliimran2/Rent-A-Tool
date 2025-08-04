package com.miassolutions.rentatool.utils.extenstions

fun Long.toFourDigitString() : String {
    return String.format("%04d", this)
}