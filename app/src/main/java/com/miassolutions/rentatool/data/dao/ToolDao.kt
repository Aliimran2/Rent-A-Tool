package com.miassolutions.rentatool.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.miassolutions.rentatool.data.entities.ToolEntity
import com.miassolutions.rentatool.data.model.ToolWithAvailability
import kotlinx.coroutines.flow.Flow

@Dao
interface ToolDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(toolEntities: List<ToolEntity>) //later will be deleted todo()

    @Insert(onConflict = OnConflictStrategy.ABORT) // prevents duplicate entries
    suspend fun insertTool(toolEntity: ToolEntity)

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
        t.totalQuantity - IFNULL(SUM(r.quantityRented - r.quantityReturned), 0) AS availableQuantity
        FROM tools t
        LEFT JOIN rental_line_items r ON r.toolId = t.toolId
        GROUP BY t.toolId
    """)
    fun getToolsWithAvailableQuantity(): Flow<List<ToolWithAvailability>>




    @Query("SELECT * FROM tools WHERE LOWER(name) = LOWER(:toolName) LIMIT 1")
    suspend fun getToolByName(toolName: String): ToolEntity?

    @Query("SELECT * FROM tools WHERE name LIKE '%' || :query || '%'")
    fun searchTools(query: String): Flow<List<ToolEntity>>
}