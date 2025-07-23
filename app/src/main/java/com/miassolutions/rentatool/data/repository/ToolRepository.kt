package com.miassolutions.rentatool.data.repository

import com.miassolutions.rentatool.data.entities.ToolEntity
import kotlinx.coroutines.flow.Flow

interface ToolRepository {
    fun getAllTools(): Flow<List<ToolEntity>>
    suspend fun getToolById(toolId: Long): ToolEntity?
    suspend fun insertTool(tool: ToolEntity): Long
    suspend fun updateTool(tool: ToolEntity)
    suspend fun deleteTool(tool: ToolEntity)
    fun searchTool(query: String): Flow<List<ToolEntity>>
}
