package com.mian.appbuilder.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mian.appbuilder.model.CustomComponentEntity
import com.mian.appbuilder.model.ProjectEntity
import com.mian.appbuilder.model.ThemeEntity

@Database(
    entities = [ProjectEntity::class, CustomComponentEntity::class, ThemeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun customComponentDao(): CustomComponentDao
    abstract fun themeDao(): ThemeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "appbuilder.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
