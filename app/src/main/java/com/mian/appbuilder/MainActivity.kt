package com.mian.appbuilder

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mian.appbuilder.codegen.HtmlGenerator
import com.mian.appbuilder.codegen.KotlinGenerator
import com.mian.appbuilder.databinding.ActivityMainBinding
import com.mian.appbuilder.designer.DesignerCanvas
import com.mian.appbuilder.model.ProjectEntity
import com.mian.appbuilder.model.TemplateCategories
import com.mian.appbuilder.model.TemplateModel
import com.mian.appbuilder.model.WidgetModel
import com.mian.appbuilder.preview.PreviewActivity
import com.mian.appbuilder.templates.TemplateLibrary
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentProjectId: Long = 0
    private var projectName: String = "My App"
    private val widgets = mutableListOf<WidgetModel>()

    private val componentTypes = listOf(
        WidgetModel.TYPE_BUTTON to "Button",
        WidgetModel.TYPE_TEXT to "Text",
        WidgetModel.TYPE_IMAGE to "Image",
        WidgetModel.TYPE_INPUT to "Input",
        WidgetModel.TYPE_LIST to "List",
        WidgetModel.TYPE_CARD to "Card",
        WidgetModel.TYPE_NAV_BAR to "Nav Bar",
        WidgetModel.TYPE_SEARCH_BAR to "Search",
        WidgetModel.TYPE_TAB to "Tabs",
        WidgetModel.TYPE_TOOLBAR to "Toolbar",
        WidgetModel.TYPE_FAB to "FAB",
        WidgetModel.TYPE_BADGE to "Badge",
        WidgetModel.TYPE_SWITCH to "Switch",
        WidgetModel.TYPE_CHECKBOX to "Check",
        WidgetModel.TYPE_RADIO to "Radio",
        WidgetModel.TYPE_PROGRESS to "Progress",
        WidgetModel.TYPE_SLIDER to "Slider",
        WidgetModel.TYPE_DIVIDER to "Divider",
        WidgetModel.TYPE_WEBVIEW to "Web",
        WidgetModel.TYPE_MAP to "Map",
        WidgetModel.TYPE_VIDEO to "Video",
        WidgetModel.TYPE_CHART to "Chart",
        WidgetModel.GRID_LAYOUT to "Grid",
        WidgetModel.STACK_LAYOUT to "Stack",
        WidgetModel.SCROLL_LAYOUT to "Scroll",
        WidgetModel.TABLE_LAYOUT to "Table"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        TemplateLibrary.init()

        setupToolbar()
        setupComponentPanel()
        setupCanvas()
        setupPropertyPanel()
        setupBottomBar()
        loadOrCreateProject()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = projectName

        binding.btnPreview.setOnClickListener {
            openPreview()
        }

        binding.btnExport.setOnClickListener {
            showExportDialog()
        }

        binding.btnUndo.visibility = View.GONE
        binding.btnRedo.visibility = View.GONE
    }

    private fun setupComponentPanel() {
        val container = binding.componentPanel
        val scrollView = HorizontalScrollView(this)
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(16, 16, 16, 16)
        }

        componentTypes.forEach { (type, label) ->
            val btn = TextView(this).apply {
                text = label
                textSize = 12f
                setPadding(32, 24, 32, 24)
                setBackgroundResource(com.google.android.material.R.drawable.material_card_shape)
                setTextColor(Color.parseColor("#6750A4"))
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(8, 0, 8, 0)
                layoutParams = lp
                setOnClickListener {
                    val widget = WidgetModel.create(type)
                    widget.x = 100f + (0..300).random()
                    widget.y = 100f + (0..400).random()
                    binding.designerCanvas.addWidget(widget)
                    widgets.add(widget)
                }
            }
            row.addView(btn)
        }

        scrollView.addView(row)
        container.addView(scrollView)
    }

    private fun setupCanvas() {
        binding.designerCanvas.onWidgetSelected = { widget ->
            updatePropertyPanel(widget)
        }
        binding.designerCanvas.onWidgetModified = {
            autoSave()
        }
    }

    private fun setupPropertyPanel() {
        // Property panel is populated dynamically
    }

    private fun updatePropertyPanel(widget: WidgetModel?) {
        val panel = binding.propertyPanel
        panel.removeAllViews()

        if (widget == null) {
            val tv = TextView(this).apply {
                text = "Select a widget to edit its properties.\n\nUse the toolbar buttons to:\n• Duplicate selected\n• Delete selected\n• Adjust properties"
                setPadding(32, 32, 32, 32)
                setTextColor(Color.GRAY)
                textSize = 14f
            }
            panel.addView(tv)
            return
        }

        val title = TextView(this).apply {
            text = "Properties: ${widget.name}"
            setPadding(24, 24, 24, 8)
            textSize = 16f
            setTextColor(Color.parseColor("#6750A4"))
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        }
        panel.addView(title)

        // Common properties
        addEditTextProperty(panel, "Name", widget.name) { widget.name = it; binding.designerCanvas.invalidate() }
        addEditTextProperty(panel, "Width", widget.width.toInt().toString()) {
            widget.width = it.toFloatOrNull() ?: widget.width
            binding.designerCanvas.updateSelectedSize(widget.width, widget.height)
        }
        addEditTextProperty(panel, "Height", widget.height.toInt().toString()) {
            widget.height = it.toFloatOrNull() ?: widget.height
            binding.designerCanvas.updateSelectedSize(widget.width, widget.height)
        }

        // Widget-specific properties
        widget.properties.forEach { (key, value) ->
            when (value) {
                is String -> addEditTextProperty(panel, key, value) {
                    widget.properties[key] = it
                    binding.designerCanvas.updateSelectedWidget(key, it)
                }
                is Number -> addSeekBarProperty(panel, key, value.toFloat()) {
                    widget.properties[key] = it
                    binding.designerCanvas.updateSelectedWidget(key, it)
                }
                is Boolean -> addSwitchProperty(panel, key, value) {
                    widget.properties[key] = it
                    binding.designerCanvas.updateSelectedWidget(key, it)
                }
            }
        }

        // Action buttons
        val btnRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(16, 24, 16, 16)
        }

        val btnDup = Button(this).apply {
            text = "Duplicate"
            setBackgroundColor(Color.parseColor("#6750A4"))
            setTextColor(Color.WHITE)
            setOnClickListener {
                binding.designerCanvas.duplicateSelected()
            }
        }
        val btnDel = Button(this).apply {
            text = "Delete"
            setBackgroundColor(Color.parseColor("#BA1A1A"))
            setTextColor(Color.WHITE)
            setOnClickListener {
                binding.designerCanvas.deleteSelected()
                updatePropertyPanel(null)
            }
        }
        btnRow.addView(btnDup)
        btnRow.addView(btnDel)
        panel.addView(btnRow)
    }

    private fun addEditTextProperty(parent: LinearLayout, label: String, value: String, onChanged: (String) -> Unit) {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 8, 24, 8)
        }
        val tvLabel = TextView(this).apply {
            text = label
            textSize = 12f
            setTextColor(Color.GRAY)
        }
        val et = EditText(this).apply {
            setText(value)
            textSize = 14f
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    onChanged(s.toString())
                }
            })
        }
        container.addView(tvLabel)
        container.addView(et)
        parent.addView(container)
    }

    private fun addSeekBarProperty(parent: LinearLayout, label: String, value: Float, onChanged: (Float) -> Unit) {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 8, 24, 8)
        }
        val tvLabel = TextView(this).apply {
            text = "$label: ${value.toInt()}"
            textSize = 12f
            setTextColor(Color.GRAY)
        }
        val sb = SeekBar(this).apply {
            max = 100
            progress = value.toInt().coerceIn(0, 100)
            setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    tvLabel.text = "$label: $progress"
                    onChanged(progress.toFloat())
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
        container.addView(tvLabel)
        container.addView(sb)
        parent.addView(container)
    }

    private fun addSwitchProperty(parent: LinearLayout, label: String, value: Boolean, onChanged: (Boolean) -> Unit) {
        val sw = Switch(this).apply {
            text = label
            isChecked = value
            setOnCheckedChangeListener { _, isChecked -> onChanged(isChecked) }
        }
        parent.addView(sw)
    }

    private fun setupBottomBar() {
        binding.btnTemplates.setOnClickListener {
            showTemplatesDialog()
        }
        binding.btnProjects.setOnClickListener {
            showProjectsDialog()
        }
        binding.btnComponents.setOnClickListener {
            showCustomComponentsDialog()
        }
        binding.btnSettings.setOnClickListener {
            showSettingsDialog()
        }
    }

    private fun showTemplatesDialog() {
        val categories = TemplateCategories.all
        val items = categories.toTypedArray()
        MaterialAlertDialogBuilder(this)
            .setTitle("Templates (${TemplateLibrary.templates.size} available)")
            .setItems(items) { _, which ->
                val selected = categories[which]
                val templates = TemplateLibrary.getByCategory(selected)
                showTemplatePicker(templates)
            }
            .show()
    }

    private fun showTemplatePicker(templates: List<TemplateModel>) {
        val names = templates.map { "${it.name}${if (it.isComplete) " ✓" else ""}" }.toTypedArray()
        MaterialAlertDialogBuilder(this)
            .setTitle("Choose Template")
            .setItems(names) { _, which ->
                val template = templates[which]
                if (template.screens.isNotEmpty()) {
                    widgets.clear()
                    widgets.addAll(template.screens)
                    binding.designerCanvas.setWidgets(widgets)
                    Toast.makeText(this, "Loaded: ${template.name}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Template '${template.name}' loaded as starting point", Toast.LENGTH_SHORT).show()
                    addDefaultWidgets()
                }
            }
            .show()
    }

    private fun addDefaultWidgets() {
        val toolbar = WidgetModel.create(WidgetModel.TYPE_TOOLBAR).apply {
            y = 0f; height = 144f; width = 1080f
            properties["title"] = projectName
        }
        val search = WidgetModel.create(WidgetModel.TYPE_SEARCH_BAR).apply {
            x = 40f; y = 180f; width = 1000f; height = 110f
        }
        val navBar = WidgetModel.create(WidgetModel.TYPE_NAV_BAR).apply {
            y = 1700f; height = 144f; width = 1080f
        }
        widgets.addAll(listOf(toolbar, search, navBar))
        binding.designerCanvas.setWidgets(widgets)
    }

    private fun showProjectsDialog() {
        lifecycleScope.launch {
            val projects = App.database.projectDao().getAll()
            projects.observe(this@MainActivity) { list ->
                val names = list.map { "${it.name} (${formatTime(it.updatedAt)})" }.toTypedArray()
                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle("Projects (${list.size})")
                    .setItems(names) { _, which ->
                        val project = list[which]
                        loadProject(project)
                    }
                    .setPositiveButton("New Project") { _, _ ->
                        newProject()
                    }
                    .setNegativeButton("Close", null)
                    .show()
            }
        }
    }

    private fun loadProject(project: ProjectEntity) {
        currentProjectId = project.id
        projectName = project.name
        supportActionBar?.title = projectName
        try {
            val type = object : TypeToken<List<WidgetModel>>() {}.type
            val loaded: List<WidgetModel> = Gson().fromJson(project.widgetsJson, type)
            widgets.clear()
            widgets.addAll(loaded)
            binding.designerCanvas.setWidgets(widgets)
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading project", Toast.LENGTH_SHORT).show()
        }
    }

    private fun newProject() {
        currentProjectId = 0
        projectName = "My App ${System.currentTimeMillis() % 1000}"
        supportActionBar?.title = projectName
        widgets.clear()
        addDefaultWidgets()
    }

    private fun showCustomComponentsDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Custom Components")
            .setItems(arrayOf("New KT Component", "New SVG Component", "New Logic Component", "View Saved Components")) { _, which ->
                when (which) {
                    0 -> showNewComponentDialog("kt")
                    1 -> showNewComponentDialog("svg")
                    2 -> showNewComponentDialog("logic")
                    3 -> showSavedComponents()
                }
            }
            .show()
    }

    private fun showNewComponentDialog(type: String) {
        val input = EditText(this).apply {
            hint = "Component name"
        }
        val codeInput = EditText(this).apply {
            hint = "Code / SVG / Logic definition"
            minLines = 5
            gravity = android.view.Gravity.TOP
        }
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
            addView(input)
            addView(codeInput)
        }
        MaterialAlertDialogBuilder(this)
            .setTitle("New ${type.uppercase()} Component")
            .setView(container)
            .setPositiveButton("Save") { _, _ ->
                val name = input.text.toString().ifEmpty { "Untitled" }
                val code = codeInput.text.toString()
                lifecycleScope.launch {
                    App.database.customComponentDao().insert(
                        com.mian.appbuilder.model.CustomComponentEntity(
                            name = name, type = type, code = code
                        )
                    )
                    Toast.makeText(this@MainActivity, "Component saved", Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }

    private fun showSavedComponents() {
        lifecycleScope.launch {
            App.database.customComponentDao().getAll().observe(this@MainActivity) { list ->
                val names = list.map { "${it.name} (${it.type})" }.toTypedArray()
                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle("Saved Components (${list.size})")
                    .setItems(names) { _, which ->
                        val comp = list[which]
                        Toast.makeText(this@MainActivity, comp.code, Toast.LENGTH_LONG).show()
                    }
                    .setNegativeButton("Close", null)
                    .show()
            }
        }
    }

    private fun showSettingsDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Settings")
            .setItems(arrayOf("Theme: Material You (M3)", "Import Theme (JAR/AAR)", "Font Settings", "About & Licenses")) { _, which ->
                when (which) {
                    0 -> Toast.makeText(this, "Current theme: Material 3 Dynamic Color", Toast.LENGTH_SHORT).show()
                    1 -> pickFile()
                    2 -> Toast.makeText(this, "Open source fonts: Roboto, Noto Sans, Lato", Toast.LENGTH_SHORT).show()
                    3 -> startActivity(Intent(this, com.mian.appbuilder.ui.AboutActivity::class.java))
                }
            }
            .show()
    }

    private val filePicker = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            Toast.makeText(this, "Theme imported: $it", Toast.LENGTH_LONG).show()
        }
    }

    private fun pickFile() {
        filePicker.launch(arrayOf("*/*"))
    }

    private fun openPreview() {
        val widgetsJson = Gson().toJson(widgets)
        val intent = Intent(this, PreviewActivity::class.java).apply {
            putExtra("widgets_json", widgetsJson)
            putExtra("project_name", projectName)
        }
        startActivity(intent)
    }

    private fun showExportDialog() {
        val options = arrayOf("Export as HTML", "Export as Kotlin (Android Project)")
        MaterialAlertDialogBuilder(this)
            .setTitle("Export Project")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> exportHtml()
                    1 -> exportKotlin()
                }
            }
            .show()
    }

    private fun exportHtml() {
        val html = HtmlGenerator.generate(widgets, projectName)
        showExportResult("HTML", html, "$projectName.html")
    }

    private fun exportKotlin() {
        val packageName = "com.generated.${projectName.lowercase().replace(" ", "")}"
        val mainActivity = KotlinGenerator.generateActivity(widgets, projectName)
        val buildGradle = KotlinGenerator.generateBuildGradle(projectName, packageName)
        val manifest = KotlinGenerator.generateAndroidManifest(packageName)
        val settings = KotlinGenerator.generateSettingsGradle(projectName)
        val rootBuild = KotlinGenerator.generateRootBuildGradle()

        val fullProject = buildString {
            appendLine("=== Generated Android Project: $projectName ===")
            appendLine("")
            appendLine("--- settings.gradle ---")
            appendLine(settings)
            appendLine("")
            appendLine("--- build.gradle (root) ---")
            appendLine(rootBuild)
            appendLine("")
            appendLine("--- app/build.gradle ---")
            appendLine(buildGradle)
            appendLine("")
            appendLine("--- AndroidManifest.xml ---")
            appendLine(manifest)
            appendLine("")
            appendLine("--- MainActivity.kt ---")
            appendLine(mainActivity)
        }
        showExportResult("Kotlin", fullProject, "$projectName_project.txt")
    }

    private fun showExportResult(format: String, code: String, filename: String) {
        val scrollView = ScrollView(this)
        val tv = TextView(this).apply {
            text = code
            setTextSize(10f)
            setTextIsSelectable(true)
            setPadding(24, 24, 24, 24)
            setTextColor(Color.parseColor("#1C1B1F"))
            setTextIsSelectable(true)
        }
        scrollView.addView(tv)

        MaterialAlertDialogBuilder(this)
            .setTitle("Exported as $format")
            .setView(scrollView)
            .setPositiveButton("Copy to Clipboard") { _, _ ->
                val clipboard = getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                clipboard.setPrimaryClip(android.content.ClipData.newPlainText(filename, code))
                Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }
            .setNeutralButton("Save to File") { _, _ ->
                saveToFile(filename, code)
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun saveToFile(filename: String, content: String) {
        try {
            val dir = java.io.File(getExternalFilesDir(null), "exports")
            if (!dir.exists()) dir.mkdirs()
            val file = java.io.File(dir, filename)
            file.writeText(content)
            Toast.makeText(this, "Saved to: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error saving: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadOrCreateProject() {
        lifecycleScope.launch {
            val count = App.database.projectDao().count()
            if (count == 0) {
                addDefaultWidgets()
            } else {
                App.database.projectDao().getAll().observe(this@MainActivity) { list ->
                    if (list.isNotEmpty() && currentProjectId == 0L) {
                        loadProject(list[0])
                    }
                }
            }
        }
    }

    private fun autoSave() {
        lifecycleScope.launch {
            val widgetsJson = Gson().toJson(widgets)
            if (currentProjectId == 0L) {
                currentProjectId = App.database.projectDao().insert(
                    ProjectEntity(
                        name = projectName,
                        widgetsJson = widgetsJson,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            } else {
                App.database.projectDao().update(
                    ProjectEntity(
                        id = currentProjectId,
                        name = projectName,
                        widgetsJson = widgetsJson,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        autoSave()
    }

    private fun formatTime(ts: Long): String {
        val sdf = java.text.SimpleDateFormat("MM-dd HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(ts))
    }
}
