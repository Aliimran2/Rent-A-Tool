package com.miassolutions.rentatool.data.repository

import com.miassolutions.rentatool.data.dao.RentalOrderDao
import com.miassolutions.rentatool.data.dao.ToolDao
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.entities.ToolEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToolRepository @Inject constructor(private val toolDao: ToolDao) {

    suspend fun insertTool(tool: ToolEntity) {
        toolDao.insertTool(tool)
    }

    fun getAllTools(): Flow<List<ToolEntity>> = toolDao.getAllTools()
    fun searchTool(query: String): Flow<List<ToolEntity>> = toolDao.searchTools(query)

}