package com.miassolutions.rentatool.data.common

sealed class CustomerResult {
    data class Failure(val message: String) : CustomerResult()
    data class Success(val customerId: Long) : CustomerResult()
}
