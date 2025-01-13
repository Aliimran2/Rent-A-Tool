package com.miassolutions.rentatool.data.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.miassolutions.rentatool.data.model.Tool

@Dao
interface ToolDao {

    @Insert(onConflict = OnConflictStrategy.ABORT) // prevents duplicate entries
    suspend fun addTool(tool: Tool)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tools: List<Tool>)

    @Update
    suspend fun updateTool(tool: Tool)

    // New method to update only the stock and rented quantity
    @Query("UPDATE tools SET availableStock = :availableStock, rentedQuantity = :rentedQuantity WHERE toolId = :toolId")
    suspend fun updateStock(toolId: Long, availableStock: Int, rentedQuantity: Int)

    @Query("SELECT * FROM tools")
    fun getAllTools() : List<Tool>

    @Query("SELECT * FROM tools WHERE toolId =:toolId")
    fun getToolById(toolId : Long) : Tool?

    @Query("SELECT * FROM tools WHERE name LIKE '%' || :query || '%'")
    fun searchTools(query: String) : List<Tool>
}