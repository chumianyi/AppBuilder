package com.mian.appbuilder.model

/**
 * Template definition for the 400+ template library.
 */
data class TemplateModel(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val iconName: String,
    val isComplete: Boolean = false,
    val configTemplate: String = "",
    val screens: List<WidgetModel> = emptyList(),
    var isFavorite: Boolean = false
)

object TemplateCategories {
    const val SOCIAL = "Social"
    const val ECOMMERCE = "E-commerce"
    const val TOOLS = "Tools"
    const val ENTERTAINMENT = "Entertainment"
    const val EDUCATION = "Education"
    const val FINANCE = "Finance"
    const val PRODUCTIVITY = "Productivity"
    const val HEALTH = "Health"
    const val TRAVEL = "Travel"
    const val FOOD = "Food & Drink"
    const val NEWS = "News"
    const val SPORTS = "Sports"
    const val PHOTOGRAPHY = "Photography"
    const val MUSIC = "Music"
    const val VIDEO = "Video"
    const val WEATHER = "Weather"
    const val CALENDAR = "Calendar"
    const val CHAT = "Chat"
    const val NOTES = "Notes"
    const val GALLERY = "Gallery"
    const val AI = "AI & Bots"

    val all = listOf(
        SOCIAL, ECOMMERCE, TOOLS, ENTERTAINMENT, EDUCATION, FINANCE,
        PRODUCTIVITY, HEALTH, TRAVEL, FOOD, NEWS, SPORTS, PHOTOGRAPHY,
        MUSIC, VIDEO, WEATHER, CALENDAR, CHAT, NOTES, GALLERY, AI
    )
}
