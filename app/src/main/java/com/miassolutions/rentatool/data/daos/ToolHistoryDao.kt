package com.miassolutions.rentatool.data.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.miassolutions.rentatool.data.model.ToolHistory

@Dao
interface ToolHistoryDao {

    // Insert a new history record (Rent or Return)
    @Insert
    suspend fun insertToolHistory(toolHistory: ToolHistory)

    // Get all tool history for a specific customer
    @Query("""
        SELECT * 
        FROM tool_history 
        WHERE customerId = :customerId 
        ORDER BY timestamp DESC
    """)
    fun getToolHistoryForCustomer(customerId: Long): LiveData<List<ToolHistory>>

    // Get all tool history for a specific tool
    @Query("""
        SELECT * 
        FROM tool_history 
        WHERE toolId = :toolId 
        ORDER BY timestamp DESC
    """)
    fun getToolHistoryForTool(toolId: Long): LiveData<List<ToolHistory>>

    // Get all tool history
    @Query("SELECT * FROM tool_history ORDER BY timestamp DESC")
    fun getAllToolHistory(): LiveData<List<ToolHistory>>

    // Get history by action type (e.g., Rent, Return)
    @Query("""
        SELECT * 
        FROM tool_history 
        WHERE transactionType = :actionType 
        ORDER BY timestamp DESC
    """)
    fun getHistoryByAction(actionType: String): LiveData<List<ToolHistory>>

    // Get total rent earned by a specific tool
    @Query("""
        SELECT SUM(totalRent) 
        FROM tool_history 
        WHERE toolId = :toolId
    """)
    suspend fun getTotalRentForTool(toolId: Long): Double

    // Get total rent earned by a specific customer
    @Query("""
        SELECT SUM(totalRent) 
        FROM tool_history 
        WHERE customerId = :customerId
    """)
    suspend fun getTotalRentForCustomer(customerId: Long): Double

    // Get history in a specific date range (useful for reports)
    @Query("""
        SELECT * 
        FROM tool_history 
        WHERE timestamp BETWEEN :startDate AND :endDate 
        ORDER BY timestamp DESC
    """)
    fun getHistoryByDateRange(startDate: Long, endDate: Long): LiveData<List<ToolHistory>>
}
