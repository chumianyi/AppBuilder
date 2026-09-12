package com.mian.appbuilder.designer

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.SeekBar
import android.widget.ProgressBar
import android.widget.LinearLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.chip.Chip
import com.mian.appbuilder.R
import com.mian.appbuilder.model.WidgetModel
import kotlin.math.abs

/**
 * Renders a single widget on the designer canvas.
 */
class WidgetView @JvmOverloads constructor(
    context: Context,
    var model: WidgetModel,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    var onTap: (() -> Unit)? = null
    var onMove: ((Float, Float) -> Unit)? = null

    private var downX = 0f
    private var downY = 0f
    private var startTouchX = 0f
    private var startTouchY = 0f
    private var moved = false

    init {
        render(model)
        isClickable = true
        isFocusable = true
    }

    fun render(w: WidgetModel) {
        model = w
        removeAllViews()
        when (w.type) {
            WidgetModel.TYPE_BUTTON -> renderButton(w)
            WidgetModel.TYPE_TEXT -> renderText(w)
            WidgetModel.TYPE_IMAGE -> renderImage(w)
            WidgetModel.TYPE_INPUT -> renderInput(w)
            WidgetModel.TYPE_LIST -> renderList(w)
            WidgetModel.TYPE_NAV_BAR -> renderNavBar(w)
            WidgetModel.TYPE_SEARCH_BAR -> renderSearchBar(w)
            WidgetModel.TYPE_CARD -> renderCard(w)
            WidgetModel.TYPE_CHECKBOX -> renderCheckBox(w)
            WidgetModel.TYPE_SWITCH -> renderSwitch(w)
            WidgetModel.TYPE_RADIO -> renderRadio(w)
            WidgetModel.TYPE_PROGRESS -> renderProgress(w)
            WidgetModel.TYPE_SLIDER -> renderSlider(w)
            WidgetModel.TYPE_TAB -> renderTab(w)
            WidgetModel.TYPE_TOOLBAR -> renderToolbar(w)
            WidgetModel.TYPE_FAB -> renderFab(w)
            WidgetModel.TYPE_BADGE -> renderBadge(w)
            WidgetModel.TYPE_DIVIDER -> renderDivider(w)
            WidgetModel.TYPE_WEBVIEW -> renderWebView(w)
            WidgetModel.TYPE_MAP -> renderPlaceholder(w, "Map", "📍")
            WidgetModel.TYPE_VIDEO -> renderPlaceholder(w, "Video Player", "🎬")
            WidgetModel.TYPE_CHART -> renderPlaceholder(w, "Chart", "📊")
            WidgetModel.GRID_LAYOUT -> renderPlaceholder(w, "Grid Layout", "🔲")
            WidgetModel.STACK_LAYOUT -> renderPlaceholder(w, "Stack Layout", "📚")
            WidgetModel.SCROLL_LAYOUT -> renderPlaceholder(w, "Scroll View", "📜")
            WidgetModel.TABLE_LAYOUT -> renderPlaceholder(w, "Table", "📋")
            else -> renderPlaceholder(w, w.name, "🧩")
        }
    }

    private fun colorOrDefault(key: String, default: String): Int {
        return try {
            Color.parseColor(model.properties[key]?.toString() ?: default)
        } catch (e: Exception) {
            Color.parseColor(default)
        }
    }

    private fun floatOrDefault(key: String, default: Float): Float {
        return (model.properties[key] as? Number)?.toFloat() ?: default
    }

    private fun renderButton(w: WidgetModel) {
        val btn = com.google.android.material.button.MaterialButton(context).apply {
            text = w.properties["text"]?.toString() ?: "Button"
            setTextColor(colorOrDefault("textColor", "#FFFFFF"))
            textSize = floatOrDefault("textSize", 16f)
            setBackgroundColor(colorOrDefault("backgroundColor", "#6750A4"))
            cornerRadius = floatOrDefault("cornerRadius", 24f)
            isAllCaps = false
            elevation = 4f
        }
        addView(btn, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    private fun renderText(w: WidgetModel) {
        val tv = TextView(context).apply {
            text = w.properties["text"]?.toString() ?: "Text"
            setTextColor(colorOrDefault("textColor", "#1C1B1F"))
            textSize = floatOrDefault("textSize", 18f)
            gravity = Gravity.CENTER
            setTypeface(typeface, if (w.properties["bold"] == true) Typeface.BOLD else Typeface.NORMAL)
        }
        addView(tv, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    private fun renderImage(w: WidgetModel) {
        val iv = ImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            setBackgroundResource(android.R.color.darker_gray)
            val tv = TextView(context).apply {
                text = "🖼️"
                textSize = 48f
                gravity = Gravity.CENTER
            }
            addView(tv, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        }
        addView(iv, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    private fun renderInput(w: WidgetModel) {
        val et = EditText(context).apply {
            hint = w.properties["hint"]?.toString() ?: "Enter text..."
            setBackgroundColor(Color.WHITE)
            setPadding(24, 24, 24, 24)
        }
        addView(et, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    private fun renderList(w: WidgetModel) {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#F5F5F5"))
        }
        val count = (w.properties["itemCount"] as? Number)?.toInt() ?: 5
        for (i in 0 until count.coerceAtMost(8)) {
            val row = android.widget.TextView(context).apply {
                text = "  List Item ${i + 1}"
                setPadding(32, 36, 32, 36)
                textSize = 15f
                setBackgroundColor(Color.WHITE)
            }
            container.addView(row)
        }
        addView(container, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    private fun renderNavBar(w: WidgetModel) {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setBackgroundColor(colorOrDefault("backgroundColor", "#FFFFFF"))
            elevation = 8f
        }
        val items = w.properties["items"] as? List<*> ?: listOf("Home", "Search", "Profile")
        items.forEach { item ->
            val tv = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
            }
            val icon = TextView(context).apply {
                text = when (item.toString().lowercase()) {
                    "home" -> "🏠"
                    "search" -> "🔍"
                    "profile", "user", "me" -> "👤"
                    "settings" -> "⚙️"
                    "notifications" -> "🔔"
                    else -> "📌"
                }
                textSize = 22f
                gravity = Gravity.CENTER
            }
            val label = TextView(context).apply {
                text = item.toString()
                textSize = 10f
                gravity = Gravity.CENTER
                setTextColor(Color.parseColor("#666666"))
            }
            tv.addView(icon)
            tv.addView(label)
            container.addView(tv)
        }
        addView(container, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    private fun renderSearchBar(w: WidgetModel) {
        val card = MaterialCardView(context).apply {
            radius = 48f
            cardElevation = 2f
            setCardBackgroundColor(colorOrDefault("backgroundColor", "#F3EDF7"))
        }
        val row = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            padding = 24
        }
        val icon = TextView(context).apply {
            text = "🔍"
            textSize = 18f
        }
        val hint = TextView(context).apply {
            text = w.properties["hint"]?.toString() ?: "Search..."
            setTextColor(Color.parseColor("#999999"))
            textSize = 15f
            setPadding(24, 0, 0, 0)
        }
        row.addView(icon)
        row.addView(hint)
        card.addView(row, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        addView(card, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    private fun renderCard(w: WidgetModel) {
        val card = MaterialCardView(context).apply {
            radius = floatOrDefault("cornerRadius", 16f)
            cardElevation = floatOrDefault("elevation", 4f)
            setCardBackgroundColor(colorOrDefault("backgroundColor", "#FFFFFF"))
        }
        val tv = TextView(context).apply {
            text = w.properties["title"]?.toString() ?: "Card Title"
            textSize = 16f
            setTextColor(Color.parseColor("#1C1B1F"))
            setPadding(32, 32, 32, 32)
        }
        card.addView(tv, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
        addView(card, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    private fun renderCheckBox(w: WidgetModel) {
        val cb = android.widget.CheckBox(context).apply {
            text = w.properties["text"]?.toString() ?: "Check me"
            textSize = 15f
        }
        addView(cb, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    }

    private fun renderSwitch(w: WidgetModel) {
        val sw = Switch(context).apply {
            text = w.properties["text"]?.toString() ?: "Switch"
            textSize = 15f
        }
        addView(sw, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    }

    private fun renderRadio(w: WidgetModel) {
        val rb = android.widget.RadioButton(context).apply {
            text = w.properties["text"]?.toString() ?: "Option"
            textSize = 15f
        }
        addView(rb, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    }

    private fun renderProgress(w: WidgetModel) {
        val pb = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal).apply {
            progress = (w.properties["progress"] as? Number)?.toInt() ?: 50
            max = 100
        }
        addView(pb, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    }

    private fun renderSlider(w: WidgetModel) {
        val sb = SeekBar(context).apply {
            progress = (w.properties["value"] as? Number)?.toInt() ?: 50
            max = 100
        }
        addView(sb, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    }

    private fun renderTab(w: WidgetModel) {
        val tl = TabLayout(context).apply {
            val tabs = w.properties["tabs"] as? List<*> ?: listOf("Tab1", "Tab2", "Tab3")
            tabs.forEach { addTab(newTab().setText(it.toString())) }
            setTabTextColors(Color.GRAY, Color.WHITE)
            setBackgroundColor(colorOrDefault("backgroundColor", "#6750A4"))
        }
        addView(tl, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    private fun renderToolbar(w: WidgetModel) {
        val container = FrameLayout(context).apply {
            setBackgroundColor(colorOrDefault("backgroundColor", "#6750A4"))
        }
        val tv = TextView(context).apply {
            text = w.properties["title"]?.toString() ?: "Toolbar"
            setTextColor(Color.WHITE)
            textSize = 18f
            gravity = Gravity.CENTER
            setPadding(48, 0, 0, 0)
        }
        container.addView(tv, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        })
        addView(container, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    private fun renderFab(w: WidgetModel) {
        val fab = FloatingActionButton(context).apply {
            setImageResource(android.R.drawable.ic_input_add)
            backgroundTintList = android.content.res.ColorStateList.valueOf(
                colorOrDefault("backgroundColor", "#6750A4")
            )
        }
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.CENTER
        addView(fab, lp)
    }

    private fun renderBadge(w: WidgetModel) {
        val chip = Chip(context).apply {
            text = w.properties["text"]?.toString() ?: "99+"
            isClickable = false
            isCheckable = false
        }
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.CENTER
        addView(chip, lp)
    }

    private fun renderDivider(w: WidgetModel) {
        val v = View(context).apply {
            setBackgroundColor(colorOrDefault("color", "#E0E0E0"))
        }
        addView(v, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    private fun renderWebView(w: WidgetModel) {
        val tv = TextView(context).apply {
            text = "🌐 WebView\n${w.properties["url"] ?: ""}"
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#E3F2FD"))
            setPadding(24, 24, 24, 24)
        }
        addView(tv, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    private fun renderPlaceholder(w: WidgetModel, title: String, emoji: String) {
        val tv = TextView(context).apply {
            text = "$emoji\n$title"
            gravity = Gravity.CENTER
            textSize = 14f
            setTextColor(Color.parseColor("#666666"))
            setBackgroundResource(android.R.color.darker_gray)
            alpha = 0.7f
        }
        addView(tv, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = x
                downY = y
                startTouchX = event.rawX
                startTouchY = event.rawY
                moved = false
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.rawX - startTouchX
                val dy = event.rawY - startTouchY
                if (abs(dx) > 10 || abs(dy) > 10) {
                    moved = true
                    onMove?.invoke(dx, dy)
                    startTouchX = event.rawX
                    startTouchY = event.rawY
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (!moved) {
                    performClick()
                    onTap?.invoke()
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
