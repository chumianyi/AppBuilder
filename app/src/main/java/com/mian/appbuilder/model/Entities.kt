package com.mian.appbuilder.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var name: String,
    var widgetsJson: String,
    var width: Int = 1080,
    var height: Int = 1920,
    var background: String = "#FFFFFF",
    var createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis(),
    var thumbnail: String = ""
)

@Entity(tableName = "custom_components")
data class CustomComponentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var name: String,
    var type: String, // "kt", "svg", "logic"
    var code: String,
    var propsJson: String = "{}",
    var iconName: String = "puzzle",
    var createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "themes")
data class ThemeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var name: String,
    var packagePath: String = "",
    var isBuiltin: Boolean = false,
    var primary: String = "#6750A4",
    var secondary: String = "#625B71",
    var tertiary: String = "#7D5260",
    var background: String = "#FFFBFE",
    var surface: String = "#FFFBFE",
    var onPrimary: String = "#FFFFFF",
    var onSecondary: String = "#FFFFFF",
    var isDark: Boolean = false
)
