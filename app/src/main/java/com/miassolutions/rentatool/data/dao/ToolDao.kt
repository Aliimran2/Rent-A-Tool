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
    SELECT 
        t.*,
        IFNULL(SUM(rt.remainingQuantity), 0) AS rentedQuantity
    FROM tools t
    LEFT JOIN rented_tools rt ON t.toolId = rt.toolId
    LEFT JOIN rental_orders ro ON rt.orderId = ro.orderId
    WHERE ro.isClosed = 0 OR ro.isClosed IS NULL
    GROUP BY t.toolId
""")
    fun getToolsWithAvailability(): Flow<List<ToolWithAvailability>>






    @Query("SELECT * FROM tools WHERE LOWER(name) = LOWER(:toolName) LIMIT 1")
    suspend fun getToolByName(toolName: String): ToolEntity?

    @Query("""
    SELECT t.*, 
    IFNULL(t.totalQuantity - 
        (SELECT SUM(rt.rentedQuantity - rt.remainingQuantity) 
         FROM rented_tools rt 
         WHERE rt.toolId = t.toolId), 
    t.totalQuantity) AS availableQuantity
    FROM tools t
    WHERE LOWER(t.name) LIKE LOWER('%' || :query || '%')
    ORDER BY t.name ASC
""")
    fun searchTools(query: String): Flow<List<ToolWithAvailability>>


    @Query("""
    SELECT t.*, 
    IFNULL(t.totalQuantity - 
        (SELECT SUM(rt.rentedQuantity - rt.remainingQuantity) 
         FROM rented_tools rt 
         WHERE rt.toolId = t.toolId), 
    t.totalQuantity) AS availableQuantity
    FROM tools t
    WHERE (:name IS NULL OR LOWER(t.name) LIKE LOWER('%' || :name || '%'))
      AND (:minAvailable IS NULL OR 
           (t.totalQuantity - IFNULL((SELECT SUM(rt.rentedQuantity - rt.remainingQuantity) 
                                      FROM rented_tools rt 
                                      WHERE rt.toolId = t.toolId), 0)) >= :minAvailable)
      AND (:minItems IS NULL OR t.totalQuantity >= :minItems)
    ORDER BY t.name ASC
""")
    fun filterTools(
        name: String?,
        minAvailable: Int?,
        minItems: Int?
    ): Flow<List<ToolWithAvailability>>


}