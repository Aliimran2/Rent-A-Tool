package com.miassolutions.rentatool.data.repositoryimpl

import com.miassolutions.rentatool.data.dao.ToolDao
import com.miassolutions.rentatool.data.entities.ToolEntity
import com.miassolutions.rentatool.data.repository.ToolRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToolRepositoryImpl @Inject constructor(private val toolDao: ToolDao) : ToolRepository {
    override fun getAllTools(): Flow<List<ToolEntity>> {
        return toolDao.getAllTools()
    }

    override suspend fun getToolById(toolId: Long): ToolEntity? {
        return toolDao.getToolById(toolId)
    }

    override suspend fun insertTool(tool: ToolEntity): Long {
        return toolDao.insertTool(tool)
    }

    override suspend fun updateTool(tool: ToolEntity) {
        toolDao.updateTool(tool)
    }

    override suspend fun deleteTool(tool: ToolEntity) {
        toolDao.delete(tool)
    }

    override fun searchTool(query: String): Flow<List<ToolEntity>> {
        return toolDao.searchTools(query)
    }
}