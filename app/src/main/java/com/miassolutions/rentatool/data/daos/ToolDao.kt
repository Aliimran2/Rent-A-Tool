package com.miassolutions.rentatool.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.miassolutions.rentatool.data.model.Tool
import kotlinx.coroutines.flow.Flow

@Dao
interface ToolDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tools: List<Tool>) //later will be deleted todo()

    @Insert(onConflict = OnConflictStrategy.ABORT) // prevents duplicate entries
    suspend fun insertTool(tool: Tool)

    @Update
    suspend fun updateTool(tool: Tool)

    @Query("SELECT * FROM tools")
    fun getAllTools() : Flow<List<Tool>>

    // Get available tools (those with stock > 0)
    @Query("SELECT * FROM tools WHERE availableStock > 0 ORDER BY name")
    fun getAvailableTools(): Flow<List<Tool>>

    @Query("SELECT * FROM tools WHERE toolId =:toolId")
    suspend fun getToolById(toolId : Long) : Tool?

    @Query("SELECT * FROM tools WHERE toolId =:toolId")
    suspend fun getToolByIdDirect(toolId : Long) : Tool?

    @Query("SELECT * FROM tools WHERE LOWER(name) = LOWER(:toolName) LIMIT 1")
    suspend fun getToolByName(toolName: String): Tool?

    @Query("SELECT * FROM tools WHERE name LIKE '%' || :query || '%'")
    fun searchTools(query: String) : Flow<List<Tool>>
}