package com.mian.appbuilder.preview

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mian.appbuilder.R
import com.mian.appbuilder.model.WidgetModel

/**
 * Live preview of the designed app on a phone/tablet frame.
 */
class PreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        val projectName = intent.getStringExtra("project_name") ?: "Preview"
        toolbar.title = "Preview: $projectName"

        val widgetsJson = intent.getStringExtra("widgets_json") ?: "[]"
        val type = object : TypeToken<List<WidgetModel>>() {}.type
        val widgets: List<WidgetModel> = Gson().fromJson(widgetsJson, type)

        val previewFrame = findViewById<FrameLayout>(R.id.previewFrame)
        val container = FrameLayout(this)
        container.setBackgroundColor(Color.WHITE)

        widgets.forEach { widget ->
            val view = createPreviewView(widget)
            view.x = widget.x
            view.y = widget.y
            container.addView(view, ViewGroup.LayoutParams(
                widget.width.toInt(), widget.height.toInt()
            ))
        }

        previewFrame.addView(container)

        // Phone/Tablet toggle
        findViewById<TextView>(R.id.btnPhone).setOnClickListener {
            previewFrame.layoutParams.width = 400
            previewFrame.requestLayout()
        }
        findViewById<TextView>(R.id.btnTablet).setOnClickListener {
            previewFrame.layoutParams.width = 800
            previewFrame.requestLayout()
        }
    }

    private fun createPreviewView(w: WidgetModel): View {
        return when (w.type) {
            WidgetModel.TYPE_BUTTON -> com.google.android.material.button.MaterialButton(this).apply {
                text = w.properties["text"]?.toString() ?: "Button"
                setBackgroundColor(Color.parseColor(w.properties["backgroundColor"]?.toString() ?: "#6750A4"))
                setTextColor(Color.parseColor(w.properties["textColor"]?.toString() ?: "#FFFFFF"))
                cornerRadius = (w.properties["cornerRadius"] as? Number)?.toInt() ?: 24
                isAllCaps = false
            }
            WidgetModel.TYPE_TEXT -> TextView(this).apply {
                text = w.properties["text"]?.toString() ?: "Text"
                setTextColor(Color.parseColor(w.properties["textColor"]?.toString() ?: "#1C1B1F"))
                textSize = ((w.properties["textSize"] as? Number)?.toFloat() ?: 18f) / 2.5f
                gravity = android.view.Gravity.CENTER
            }
            WidgetModel.TYPE_INPUT -> android.widget.EditText(this).apply {
                hint = w.properties["hint"]?.toString() ?: "Enter text..."
                setBackgroundColor(Color.WHITE)
            }
            WidgetModel.TYPE_CARD -> com.google.android.material.card.MaterialCardView(this).apply {
                radius = 16f
                cardElevation = 4f
                addView(TextView(this@PreviewActivity).apply {
                    text = w.properties["title"]?.toString() ?: "Card"
                    setPadding(24, 24, 24, 24)
                })
            }
            WidgetModel.TYPE_TOOLBAR -> androidx.appcompat.widget.Toolbar(this).apply {
                title = w.properties["title"]?.toString() ?: "Toolbar"
                setBackgroundColor(Color.parseColor(w.properties["backgroundColor"]?.toString() ?: "#6750A4"))
                setTitleTextColor(Color.WHITE)
            }
            WidgetModel.TYPE_SEARCH_BAR -> LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setBackgroundColor(Color.parseColor("#F3EDF7"))
                addView(TextView(context).apply {
                    text = "🔍 ${w.properties["hint"] ?: "Search"}"
                    setPadding(16, 12, 16, 12)
                    setTextColor(Color.GRAY)
                })
            }
            WidgetModel.TYPE_NAV_BAR -> LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setBackgroundColor(Color.WHITE)
                elevation = 8f
                val items = w.properties["items"] as? List<*> ?: listOf("Home", "Search", "Profile")
                items.forEach { item ->
                    addView(TextView(context).apply {
                        text = item.toString()
                        gravity = android.view.Gravity.CENTER
                        layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
                    })
                }
            }
            WidgetModel.TYPE_LIST -> LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                val count = (w.properties["itemCount"] as? Number)?.toInt() ?: 5
                repeat(count.coerceAtMost(8)) { i ->
                    addView(TextView(context).apply {
                        text = "  List Item ${i + 1}"
                        setPadding(24, 20, 24, 20)
                    })
                }
            }
            WidgetModel.TYPE_PROGRESS -> android.widget.ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
                progress = (w.properties["progress"] as? Number)?.toInt() ?: 50
            }
            WidgetModel.TYPE_SLIDER -> android.widget.SeekBar(this).apply {
                progress = (w.properties["value"] as? Number)?.toInt() ?: 50
            }
            WidgetModel.TYPE_SWITCH -> android.widget.Switch(this).apply {
                text = w.properties["text"]?.toString() ?: "Switch"
            }
            WidgetModel.TYPE_CHECKBOX -> android.widget.CheckBox(this).apply {
                text = w.properties["text"]?.toString() ?: "Check"
            }
            WidgetModel.TYPE_RADIO -> android.widget.RadioButton(this).apply {
                text = w.properties["text"]?.toString() ?: "Option"
            }
            WidgetModel.TYPE_TAB -> com.google.android.material.tabs.TabLayout(this).apply {
                val tabs = w.properties["tabs"] as? List<*> ?: listOf("Tab1", "Tab2", "Tab3")
                tabs.forEach { addTab(newTab().setText(it.toString())) }
            }
            WidgetModel.TYPE_FAB -> com.google.android.material.floatingactionbutton.FloatingActionButton(this).apply {
                setImageResource(android.R.drawable.ic_input_add)
            }
            WidgetModel.TYPE_BADGE -> com.google.android.material.chip.Chip(this).apply {
                text = w.properties["text"]?.toString() ?: "99+"
            }
            WidgetModel.TYPE_DIVIDER -> View(this).apply {
                setBackgroundColor(Color.parseColor("#E0E0E0"))
            }
            WidgetModel.TYPE_IMAGE -> android.widget.ImageView(this).apply {
                setBackgroundColor(Color.LTGRAY)
                scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
            }
            else -> TextView(this).apply {
                text = w.name
                gravity = android.view.Gravity.CENTER
                setBackgroundColor(Color.parseColor("#EEEEEE"))
                setTextColor(Color.GRAY)
            }
        }
    }
}
