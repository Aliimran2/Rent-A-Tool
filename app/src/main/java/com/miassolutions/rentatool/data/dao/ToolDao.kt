package com.miassolutions.rentatool.data.dao

import androidx.room.*
import com.miassolutions.rentatool.data.entities.ToolEntity
import com.miassolutions.rentatool.data.relationship.ToolWithAvailability
import kotlinx.coroutines.flow.Flow

@Dao
interface ToolDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTool(toolEntity: ToolEntity): Long

    @Delete
    suspend fun delete(tool: ToolEntity)

    @Update
    suspend fun updateTool(toolEntity: ToolEntity)

    @Query("SELECT * FROM tools ORDER BY name ASC")
    fun getAllTools(): Flow<List<ToolEntity>>

    @Query("SELECT * FROM tools WHERE toolId = :toolId")
    suspend fun getToolById(toolId: Long): ToolEntity?

    @Query("SELECT * FROM tools WHERE LOWER(name) = LOWER(:toolName) LIMIT 1")
    suspend fun getToolByName(toolName: String): ToolEntity?

    // ✅ Updated: Get all tools with correct availableQuantity
    @Query("""
        SELECT 
            t.*,
            (t.totalQuantity - IFNULL(SUM(rt.rentedQuantity - IFNULL(rtd.returnedQuantityTotal, 0)), 0)) AS availableQuantity
        FROM tools t
        LEFT JOIN rented_tools rt ON t.toolId = rt.toolId
        LEFT JOIN (
            SELECT rentedToolId, SUM(returnedQuantity) AS returnedQuantityTotal
            FROM returned_tools
            GROUP BY rentedToolId
        ) rtd ON rt.rentedToolId = rtd.rentedToolId
        GROUP BY t.toolId
        ORDER BY t.name ASC
    """)
    fun getToolsWithAvailability(): Flow<List<ToolWithAvailability>>

    // ✅ Updated: Search tools with correct availability
    @Query("""
        SELECT 
            t.*,
            (t.totalQuantity - IFNULL(SUM(rt.rentedQuantity - IFNULL(rtd.returnedQuantityTotal, 0)), 0)) AS availableQuantity
        FROM tools t
        LEFT JOIN rented_tools rt ON t.toolId = rt.toolId
        LEFT JOIN (
            SELECT rentedToolId, SUM(returnedQuantity) AS returnedQuantityTotal
            FROM returned_tools
            GROUP BY rentedToolId
        ) rtd ON rt.rentedToolId = rtd.rentedToolId
        WHERE LOWER(t.name) LIKE LOWER('%' || :query || '%')
        GROUP BY t.toolId
        ORDER BY t.name ASC
    """)
    fun searchTools(query: String): Flow<List<ToolWithAvailability>>

    // ✅ Updated: Filter tools with correct availability
    @Query("""
        SELECT 
            t.*,
            (t.totalQuantity - IFNULL(SUM(rt.rentedQuantity - IFNULL(rtd.returnedQuantityTotal, 0)), 0)) AS availableQuantity
        FROM tools t
        LEFT JOIN rented_tools rt ON t.toolId = rt.toolId
        LEFT JOIN (
            SELECT rentedToolId, SUM(returnedQuantity) AS returnedQuantityTotal
            FROM returned_tools
            GROUP BY rentedToolId
        ) rtd ON rt.rentedToolId = rtd.rentedToolId
        WHERE (:name IS NULL OR LOWER(t.name) LIKE LOWER('%' || :name || '%'))
        GROUP BY t.toolId
        HAVING 
            (:minAvailable IS NULL OR availableQuantity >= :minAvailable) AND
            (:minItems IS NULL OR t.totalQuantity >= :minItems)
        ORDER BY t.name ASC
    """)
    fun filterTools(
        name: String?,
        minAvailable: Int?,
        minItems: Int?
    ): Flow<List<ToolWithAvailability>>
}
