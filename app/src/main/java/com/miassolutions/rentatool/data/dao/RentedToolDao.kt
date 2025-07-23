package com.miassolutions.rentatool.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.miassolutions.rentatool.data.entities.RentedToolEntity

@Dao
interface RentedToolDao {

    @Insert
    suspend fun insertRentedTools(tools: List<RentedToolEntity>)

    @Update
    suspend fun updateRentedTool(tool: RentedToolEntity)

    @Query("SELECT * FROM rented_tools WHERE orderId = :orderId")
    suspend fun getRentedToolsForOrder(orderId: Long): List<RentedToolEntity>

}