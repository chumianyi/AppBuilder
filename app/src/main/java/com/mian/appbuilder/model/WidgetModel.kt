package com.mian.appbuilder.model

import android.graphics.Color
import org.json.JSONObject
import java.util.UUID

/**
 * Represents a single widget placed on the designer canvas.
 */
data class WidgetModel(
    var id: String = UUID.randomUUID().toString(),
    var type: String = TYPE_BUTTON,
    var name: String = "Button",
    var x: Float = 200f,
    var y: Float = 200f,
    var width: Float = 200f,
    var height: Float = 100f,
    var properties: MutableMap<String, Any> = mutableMapOf()
) {
    companion object {
        const val TYPE_BUTTON = "button"
        const val TYPE_TEXT = "text"
        const val TYPE_IMAGE = "image"
        const val TYPE_INPUT = "input"
        const val TYPE_LIST = "list"
        const val TYPE_NAV_BAR = "nav_bar"
        const val TYPE_SEARCH_BAR = "search_bar"
        const val TYPE_CARD = "card"
        const val TYPE_CHECKBOX = "checkbox"
        const val TYPE_SWITCH = "switch"
        const val TYPE_RADIO = "radio"
        const val TYPE_PROGRESS = "progress"
        const val TYPE_SLIDER = "slider"
        const val TYPE_TAB = "tab"
        const val TYPE_TOOLBAR = "toolbar"
        const val TYPE_FAB = "fab"
        const val TYPE_BADGE = "badge"
        const val TYPE_DIVIDER = "divider"
        const val TYPE_SPACER = "spacer"
        const val TYPE_WEBVIEW = "webview"
        const val TYPE_MAP = "map"
        const val TYPE_VIDEO = "video"
        const val TYPE_CHART = "chart"
        const val TABLE_LAYOUT = "table"
        const val GRID_LAYOUT = "grid"
        const val STACK_LAYOUT = "stack"
        const val SCROLL_LAYOUT = "scroll"

        fun create(type: String): WidgetModel {
            return when (type) {
                TYPE_BUTTON -> WidgetModel(type = type, name = "Button",
                    width = 300f, height = 120f,
                    properties = mutableMapOf(
                        "text" to "Click Me",
                        "backgroundColor" to "#6750A4",
                        "textColor" to "#FFFFFF",
                        "cornerRadius" to 24f,
                        "textSize" to 16f
                    ))
                TYPE_TEXT -> WidgetModel(type = type, name = "Text",
                    width = 400f, height = 80f,
                    properties = mutableMapOf(
                        "text" to "Hello World",
                        "textColor" to "#1C1B1F",
                        "textSize" to 18f,
                        "bold" to false,
                        "italic" to false
                    ))
                TYPE_IMAGE -> WidgetModel(type = type, name = "Image",
                    width = 300f, height = 300f,
                    properties = mutableMapOf(
                        "src" to "ic_placeholder",
                        "scaleType" to "fitCenter",
                        "cornerRadius" to 16f
                    ))
                TYPE_INPUT -> WidgetModel(type = type, name = "Input",
                    width = 400f, height = 120f,
                    properties = mutableMapOf(
                        "hint" to "Enter text...",
                        "inputType" to "text",
                        "label" to "Label"
                    ))
                TYPE_LIST -> WidgetModel(type = type, name = "List",
                    width = 600f, height = 600f,
                    properties = mutableMapOf(
                        "itemCount" to 10,
                        "divider" to true
                    ))
                TYPE_NAV_BAR -> WidgetModel(type = type, name = "Bottom Nav",
                    width = 1080f, height = 144f,
                    properties = mutableMapOf(
                        "items" to listOf("Home", "Search", "Profile"),
                        "backgroundColor" to "#FFFFFF"
                    ))
                TYPE_SEARCH_BAR -> WidgetModel(type = type, name = "Search Bar",
                    width = 900f, height = 110f,
                    properties = mutableMapOf(
                        "hint" to "Search...",
                        "backgroundColor" to "#F3EDF7"
                    ))
                TYPE_CARD -> WidgetModel(type = type, name = "Card",
                    width = 500f, height = 300f,
                    properties = mutableMapOf(
                        "title" to "Card Title",
                        "elevation" to 4f,
                        "cornerRadius" to 16f,
                        "backgroundColor" to "#FFFFFF"
                    ))
                TYPE_CHECKBOX -> WidgetModel(type = type, name = "Checkbox",
                    width = 200f, height = 80f,
                    properties = mutableMapOf("text" to "Check me"))
                TYPE_SWITCH -> WidgetModel(type = type, name = "Switch",
                    width = 200f, height = 80f,
                    properties = mutableMapOf("text" to "Switch"))
                TYPE_RADIO -> WidgetModel(type = type, name = "Radio Button",
                    width = 200f, height = 80f,
                    properties = mutableMapOf("text" to "Option"))
                TYPE_PROGRESS -> WidgetModel(type = type, name = "Progress",
                    width = 400f, height = 40f,
                    properties = mutableMapOf("progress" to 50))
                TYPE_SLIDER -> WidgetModel(type = type, name = "Slider",
                    width = 400f, height = 80f,
                    properties = mutableMapOf("value" to 50))
                TYPE_TAB -> WidgetModel(type = type, name = "Tab Layout",
                    width = 600f, height = 120f,
                    properties = mutableMapOf("tabs" to listOf("Tab1", "Tab2", "Tab3")))
                TYPE_TOOLBAR -> WidgetModel(type = type, name = "Toolbar",
                    width = 1080f, height = 144f,
                    properties = mutableMapOf("title" to "Toolbar", "backgroundColor" to "#6750A4"))
                TYPE_FAB -> WidgetModel(type = type, name = "FAB",
                    width = 120f, height = 120f,
                    properties = mutableMapOf("icon" to "add", "backgroundColor" to "#6750A4"))
                TYPE_BADGE -> WidgetModel(type = type, name = "Badge",
                    width = 80f, height = 80f,
                    properties = mutableMapOf("text" to "99+", "backgroundColor" to "#BA1A1A"))
                TYPE_DIVIDER -> WidgetModel(type = type, name = "Divider",
                    width = 800f, height = 4f,
                    properties = mutableMapOf("color" to "#E0E0E0"))
                TYPE_SPACER -> WidgetModel(type = type, name = "Spacer",
                    width = 200f, height = 80f,
                    properties = mutableMapOf())
                TYPE_WEBVIEW -> WidgetModel(type = type, name = "WebView",
                    width = 600f, height = 400f,
                    properties = mutableMapOf("url" to "https://example.com"))
                TYPE_MAP -> WidgetModel(type = type, name = "Map",
                    width = 600f, height = 400f,
                    properties = mutableMapOf("latitude" to 39.9, "longitude" to 116.4))
                TYPE_VIDEO -> WidgetModel(type = type, name = "Video",
                    width = 600f, height = 350f,
                    properties = mutableMapOf("url" to "", "autoplay" to false))
                TYPE_CHART -> WidgetModel(type = type, name = "Chart",
                    width = 600f, height = 400f,
                    properties = mutableMapOf("chartType" to "bar", "data" to "10,20,30,40,50"))
                TABLE_LAYOUT -> WidgetModel(type = type, name = "Table",
                    width = 600f, height = 400f,
                    properties = mutableMapOf("rows" to 5, "cols" to 3))
                GRID_LAYOUT -> WidgetModel(type = type, name = "Grid",
                    width = 600f, height = 400f,
                    properties = mutableMapOf("spanCount" to 3, "itemCount" to 9))
                STACK_LAYOUT -> WidgetModel(type = type, name = "Stack",
                    width = 600f, height = 400f,
                    properties = mutableMapOf("orientation" to "vertical"))
                SCROLL_LAYOUT -> WidgetModel(type = type, name = "Scroll",
                    width = 600f, height = 600f,
                    properties = mutableMapOf("orientation" to "vertical"))
                else -> WidgetModel(type = type, name = type)
            }
        }
    }

    fun toJson(): JSONObject {
        val obj = JSONObject()
        obj.put("id", id)
        obj.put("type", type)
        obj.put("name", name)
        obj.put("x", x)
        obj.put("y", y)
        obj.put("width", width)
        obj.put("height", height)
        val props = JSONObject()
        properties.forEach { (k, v) ->
            when (v) {
                is Number -> props.put(k, v)
                is Boolean -> props.put(k, v)
                is String -> props.put(k, v)
                is List<*> -> props.put(k, org.json.JSONArray(v.toString()))
                else -> props.put(k, v.toString())
            }
        }
        obj.put("properties", props)
        return obj
    }

    fun deepCopy(): WidgetModel {
        return copy(
            id = UUID.randomUUID().toString(),
            properties = HashMap(properties)
        )
    }
}
