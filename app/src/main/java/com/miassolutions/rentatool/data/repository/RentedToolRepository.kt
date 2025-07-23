package com.miassolutions.rentatool.data.repository

import com.miassolutions.rentatool.data.entities.RentedToolEntity

interface RentedToolRepository {
    suspend fun insertRentedTools(tools: List<RentedToolEntity>)
    suspend fun getRentedToolsByOrder(orderId: Long): List<RentedToolEntity>
    suspend fun updateRentedTool(tool: RentedToolEntity)
}
