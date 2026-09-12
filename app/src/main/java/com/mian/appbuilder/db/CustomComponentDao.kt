package com.mian.appbuilder.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mian.appbuilder.model.CustomComponentEntity

@Dao
interface CustomComponentDao {
    @Query("SELECT * FROM custom_components ORDER BY createdAt DESC")
    fun getAll(): LiveData<List<CustomComponentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(component: CustomComponentEntity): Long

    @Delete
    suspend fun delete(component: CustomComponentEntity)

    @Query("DELETE FROM custom_components WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM custom_components")
    suspend fun count(): Int
}
