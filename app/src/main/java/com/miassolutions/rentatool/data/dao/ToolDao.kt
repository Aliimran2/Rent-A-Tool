package com.miassolutions.rentatool.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.miassolutions.rentatool.data.model.ToolEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ToolDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(toolEntities: List<ToolEntity>) //later will be deleted todo()

    @Insert(onConflict = OnConflictStrategy.ABORT) // prevents duplicate entries
    suspend fun insertTool(toolEntity: ToolEntity)

    @Update
    suspend fun updateTool(toolEntity: ToolEntity)

    @Query("SELECT * FROM tools")
    fun getAllTools() : Flow<List<ToolEntity>>

    // Get available tools (those with stock > 0)
    @Query("SELECT * FROM tools WHERE totalQuantity > 0 ORDER BY name")
    fun getAvailableTools(): Flow<List<ToolEntity>>

    @Query("SELECT * FROM tools WHERE toolId =:toolId")
    suspend fun getToolById(toolId : Long) : ToolEntity?

    @Query("SELECT * FROM tools WHERE toolId =:toolId")
    suspend fun getToolByIdDirect(toolId : Long) : ToolEntity?

    @Query("SELECT * FROM tools WHERE LOWER(name) = LOWER(:toolName) LIMIT 1")
    suspend fun getToolByName(toolName: String): ToolEntity?

    @Query("SELECT * FROM tools WHERE name LIKE '%' || :query || '%'")
    fun searchTools(query: String) : Flow<List<ToolEntity>>
}