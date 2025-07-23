package com.miassolutions.rentatool.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.miassolutions.rentatool.data.entities.ToolEntity
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

    @Query("UPDATE tools SET availableQuantity = availableQuantity -:qty WHERE toolId =:toolId ")
    suspend fun decreaseToolQuantity(toolId: Long, qty : Int)

    @Query("UPDATE tools SET availableQuantity = availableQuantity +:qty WHERE toolId =:toolId ")
    suspend fun increaseToolQuantity(toolId: Long, qty : Int)




    @Query("SELECT * FROM tools WHERE LOWER(name) = LOWER(:toolName) LIMIT 1")
    suspend fun getToolByName(toolName: String): ToolEntity?

    @Query("SELECT * FROM tools WHERE name LIKE '%' || :query || '%'")
    fun searchTools(query: String): Flow<List<ToolEntity>>
}