package com.miassolutions.rentatool.uimodels

sealed class CustomerFormResult {
    data class Failure(val errorCode: Int = 0, val message: String = "") : CustomerFormResult()
    data object Success : CustomerFormResult()
}