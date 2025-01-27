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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tools: List<Tool>) //later will be deleted todo()

    @Insert(onConflict = OnConflictStrategy.ABORT) // prevents duplicate entries
    suspend fun insertTool(tool: Tool)

    @Update
    suspend fun updateTool(tool: Tool)

    @Query("SELECT * FROM tools")
    fun getAllTools() : LiveData<List<Tool>>

    // Get available tools (those with stock > 0)
    @Query("SELECT * FROM tools WHERE availableStock > 0 ORDER BY name")
    fun getAvailableTools(): LiveData<List<Tool>>

    @Query("SELECT * FROM tools WHERE toolId =:toolId")
    fun getToolById(toolId : Long) : LiveData<Tool?>

    @Query("SELECT * FROM tools WHERE toolId =:toolId")
    suspend fun getToolByIdDirect(toolId : Long) : Tool?


    //update tool stock (for renting or returning tools)
    @Query("""
           UPDATE tools
            SET availableStock = availableStock -:rentedQuantity,
            rentedQuantity = rentedQuantity +:rentedQuantity
            WHERE toolId =:toolId
            
        """)
    suspend fun updateToolStock(toolId: Long, rentedQuantity: Int)

    @Query("""
        UPDATE tools
        SET availableStock = availableStock +:returnedQuantity,
        rentedQuantity = rentedQuantity -:returnedQuantity
        WHERE toolId =:toolId
    """)

    suspend fun updateReturnedToolStock(toolId: Long, returnedQuantity : Int)


    // New method to update only the stock and rented quantity
    @Query("UPDATE tools SET availableStock = :availableStock, rentedQuantity = :rentedQuantity WHERE toolId = :toolId")
    suspend fun updateStock(toolId: Long, availableStock: Int, rentedQuantity: Int)







    @Query("SELECT * FROM tools WHERE LOWER(name) = LOWER(:toolName) LIMIT 1")
    suspend fun getToolByName(toolName: String): Tool?

    @Query("SELECT * FROM tools WHERE name LIKE '%' || :query || '%'")
    fun searchTools(query: String) : List<Tool>
}