package com.mian.appbuilder.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mian.appbuilder.model.ThemeEntity

@Dao
interface ThemeDao {
    @Query("SELECT * FROM themes")
    fun getAll(): LiveData<List<ThemeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(theme: ThemeEntity): Long

    @Delete
    suspend fun delete(theme: ThemeEntity)

    @Query("DELETE FROM themes WHERE id = :id")
    suspend fun deleteById(id: Long)
}
