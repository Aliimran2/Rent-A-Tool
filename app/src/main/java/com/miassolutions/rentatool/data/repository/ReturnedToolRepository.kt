package com.miassolutions.rentatool.data.repository

import com.miassolutions.rentatool.data.entities.ReturnedToolEntity

interface ReturnedToolRepository {
    suspend fun insertReturnedTools(tools: List<ReturnedToolEntity>)
    suspend fun getReturnedToolsByOrder(orderId: Long): List<ReturnedToolEntity>
}
