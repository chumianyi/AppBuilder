package com.mian.appbuilder.designer

import android.content.ClipData
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.DragEvent
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mian.appbuilder.model.WidgetModel

/**
 * The main designer canvas where widgets are placed, moved, resized and selected.
 */
class DesignerCanvas @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    var onWidgetSelected: ((WidgetModel?) -> Unit)? = null
    var onWidgetModified: ((WidgetModel) -> Unit)? = null
    var onWidgetsChanged: (() -> Unit)? = null

    var gridSize: Float = 24f
    var showGrid: Boolean = true

    private var selectedWidget: WidgetModel? = null
    private var selectedView: WidgetView? = null

    private val gridPaint = Paint().apply {
        color = Color.parseColor("#22000000")
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }

    private val selectionPaint = Paint().apply {
        color = Color.parseColor("#6750A4")
        style = Paint.Style.STROKE
        strokeWidth = 3f
        pathEffect = DashPathEffect(floatArrayOf(12f, 8f), 0f)
    }

    private val handles = mutableListOf<Rect>()
    private val handleSize = 40f
    private val handlePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    private val handleBorderPaint = Paint().apply {
        color = Color.parseColor("#6750A4")
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private var activeHandle: Int = -1
    private val handleN = 0
    private val handleS = 1
    private val handleE = 2
    private val handleW = 3
    private val handleNE = 4
    private val handleNW = 5
    private val handleSE = 6
    private val handleSW = 7

    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var dragOffsetX = 0f
    private var dragOffsetY = 0f

    var widgets: List<WidgetModel> = emptyList()
        private set

    init {
        setBackgroundColor(Color.parseColor("#FAFAFA"))
        isClickable = true
        isFocusable = true
        descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS
    }

    fun setWidgets(widgets: List<WidgetModel>) {
        this.widgets = widgets
        removeAllViews()
        selectedWidget = null
        selectedView = null
        widgets.forEach { addWidgetView(it) }
        onWidgetsChanged?.invoke()
    }

    fun addWidget(widget: WidgetModel) {
        widgets = widgets + widget
        addWidgetView(widget)
        onWidgetsChanged?.invoke()
    }

    private fun addWidgetView(widget: WidgetModel) {
        val wv = WidgetView(context, widget)
        wv.layoutParams = LayoutParams(widget.width.toInt(), widget.height.toInt())
        wv.x = widget.x
        wv.y = widget.y
        wv.onTap = {
            selectWidget(widget, wv)
        }
        wv.onMove = { dx, dy ->
            widget.x += dx
            widget.y += dy
            wv.x = widget.x
            wv.y = widget.y
            onWidgetModified?.invoke(widget)
            invalidate()
        }
        addView(wv)
    }

    fun selectWidget(widget: WidgetModel?, view: WidgetView? = null) {
        selectedWidget = widget
        selectedView = view
        invalidate()
        onWidgetSelected?.invoke(widget)
    }

    fun deleteSelected() {
        selectedWidget?.let { w ->
            selectedView?.let { removeView(it) }
            widgets = widgets.filter { it.id != w.id }
            selectedWidget = null
            selectedView = null
            onWidgetsChanged?.invoke()
            invalidate()
        }
    }

    fun duplicateSelected() {
        selectedWidget?.let { w ->
            val copy = w.deepCopy()
            copy.x = w.x + 40f
            copy.y = w.y + 40f
            addWidget(copy)
        }
    }

    fun updateSelectedWidget(propKey: String, value: Any) {
        selectedWidget?.let { w ->
            w.properties[propKey] = value
            selectedView?.render(w)
            onWidgetModified?.invoke(w)
        }
    }

    fun updateSelectedSize(w: Float, h: Float) {
        selectedWidget?.let {
            it.width = w
            it.height = h
            selectedView?.layoutParams?.width = w.toInt()
            selectedView?.layoutParams?.height = h.toInt()
            selectedView?.requestLayout()
            invalidate()
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (showGrid) drawGrid(canvas)
        selectedWidget?.let { drawSelection(canvas, it) }
    }

    private fun drawGrid(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()
        var x = 0f
        while (x < w) {
            canvas.drawLine(x, 0f, x, h, gridPaint)
            x += gridSize
        }
        var y = 0f
        while (y < h) {
            canvas.drawLine(0f, y, w, y, gridPaint)
            y += gridSize
        }
    }

    private fun drawSelection(canvas: Canvas, w: WidgetModel) {
        val left = w.x
        val top = w.y
        val right = w.x + w.width
        val bottom = w.y + w.height

        canvas.drawRect(left, top, right, bottom, selectionPaint)

        handles.clear()
        // 8 handles
        val points = listOf(
            Pair(left + w.width / 2, top),       // N
            Pair(left + w.width / 2, bottom),     // S
            Pair(right, top + w.height / 2),     // E
            Pair(left, top + w.height / 2),      // W
            Pair(right, top),                     // NE
            Pair(left, top),                     // NW
            Pair(right, bottom),                 // SE
            Pair(left, bottom)                   // SW
        )

        points.forEach { (px, py) ->
            val rect = Rect(
                (px - handleSize / 2).toInt(),
                (py - handleSize / 2).toInt(),
                (px + handleSize / 2).toInt(),
                (py + handleSize / 2).toInt()
            )
            handles.add(rect)
            canvas.drawRect(rect, handlePaint)
            canvas.drawRect(rect, handleBorderPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                activeHandle = hitHandle(x, y)
                lastTouchX = x
                lastTouchY = y

                if (activeHandle == -1) {
                    // Check if tapped on empty area -> deselect
                    if (selectedWidget != null && !isOnWidget(x, y)) {
                        selectWidget(null)
                    }
                }
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = x - lastTouchX
                val dy = y - lastTouchY
                lastTouchX = x
                lastTouchY = y

                selectedWidget?.let { w ->
                    if (activeHandle >= 0) {
                        resizeWidget(w, activeHandle, dx, dy)
                        selectedView?.render(w)
                        onWidgetModified?.invoke(w)
                    }
                }
                invalidate()
                return true
            }

            MotionEvent.ACTION_UP -> {
                activeHandle = -1
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun hitHandle(x: Float, y: Float): Int {
        handles.forEachIndexed { index, rect ->
            if (rect.contains(x.toInt(), y.toInt())) return index
        }
        return -1
    }

    private fun isOnWidget(x: Float, y: Float): Boolean {
        selectedWidget?.let { w ->
            return x >= w.x && x <= w.x + w.width && y >= w.y && y <= w.y + w.height
        }
        return false
    }

    private fun resizeWidget(w: WidgetModel, handle: Int, dx: Float, dy: Float) {
        when (handle) {
            handleN -> { w.y += dy; w.height -= dy }
            handleS -> { w.height += dy }
            handleE -> { w.width += dx }
            handleW -> { w.x += dx; w.width -= dx }
            handleNE -> { w.y += dy; w.height -= dy; w.width += dx }
            handleNW -> { w.x += dx; w.y += dy; w.width -= dx; w.height -= dy }
            handleSE -> { w.width += dx; w.height += dy }
            handleSW -> { w.x += dx; w.width -= dx; w.height += dy }
        }
        if (w.width < 80) { w.width = 80f; if (handle == handleW || handle == handleNW || handle == handleSW) w.x -= dx }
        if (w.height < 40) { w.height = 40f; if (handle == handleN || handle == handleNW || handle == handleNE) w.y -= dy }
    }

    fun clearSelection() {
        selectWidget(null)
    }
}
