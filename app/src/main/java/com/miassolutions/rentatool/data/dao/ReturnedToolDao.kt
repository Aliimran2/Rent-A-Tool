package com.miassolutions.rentatool.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.miassolutions.rentatool.data.entities.ReturnedToolEntity

@Dao
interface ReturnedToolDao {
    @Insert
    suspend fun insertReturnedTool(tool : ReturnedToolEntity)

    @Query("SELECT * FROM returned_tools WHERE rentedToolId = :rentedToolId")
    suspend fun getReturnsForRentedTool(rentedToolId : Long) : List<ReturnedToolEntity>
}