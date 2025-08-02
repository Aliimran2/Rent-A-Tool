package com.miassolutions.rentatool.data.entities

data class RentedToolWithReturnStatus(
    val rentedToolId: Long,
    val toolId: Long,
    val toolName: String,
    val rentedQuantity: Int,
    val returnedQuantity: Int,
    val remainingQuantity: Int
)

