package com.miassolutions.rentatool.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.miassolutions.rentatool.data.entities.ToolEntity
import com.miassolutions.rentatool.data.relationship.ToolWithAvailability
import kotlinx.coroutines.flow.Flow

@Dao
interface ToolDao {


    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTool(toolEntity: ToolEntity) : Long

    @Delete
    suspend fun delete(tool: ToolEntity)

    @Update
    suspend fun updateTool(toolEntity: ToolEntity)

    @Query("SELECT * FROM tools ORDER BY name ASC")
    fun getAllTools(): Flow<List<ToolEntity>>

    @Query("SELECT * FROM tools WHERE toolId =:toolId")
    suspend fun getToolById(toolId: Long): ToolEntity?

    @Query("""
        SELECT t.*, 
        IFNULL(t.totalQuantity - 
            (SELECT SUM(rt.rentedQuantity - rt.remainingQuantity) 
             FROM rented_tools rt 
             WHERE rt.toolId = t.toolId), 
        t.totalQuantity) AS availableQuantity
        FROM tools t
        ORDER BY t.name ASC
    """)
    fun getToolsWithAvailability(): Flow<List<ToolWithAvailability>>






    @Query("SELECT * FROM tools WHERE LOWER(name) = LOWER(:toolName) LIMIT 1")
    suspend fun getToolByName(toolName: String): ToolEntity?

    @Query("SELECT * FROM tools WHERE name LIKE '%' || :query || '%'")
    fun searchTools(query: String): Flow<List<ToolEntity>>
}