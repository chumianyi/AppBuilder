package com.mian.appbuilder.codegen

import com.mian.appbuilder.model.WidgetModel

/**
 * Generates complete HTML+CSS+JS code from the designer canvas.
 */
object HtmlGenerator {

    fun generate(widgets: List<WidgetModel>, projectName: String): String {
        val bodyElements = widgets.joinToString("\n        ") { buildHtmlElement(it) }

        return """<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>$projectName</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; -webkit-tap-highlight-color: transparent; }
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', sans-serif;
            background: #f5f5f5;
            min-height: 100vh;
            overflow-x: hidden;
        }
        .app-container {
            position: relative;
            width: 100%;
            max-width: 480px;
            margin: 0 auto;
            min-height: 100vh;
            background: #fff;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }
        .widget { position: absolute; }
        .btn {
            display: inline-flex; align-items: center; justify-content: center;
            padding: 12px 24px; border-radius: 24px; border: none;
            font-size: 16px; font-weight: 500; cursor: pointer;
            transition: all 0.2s;
        }
        .btn:active { transform: scale(0.96); }
        .text { display: flex; align-items: center; padding: 8px; }
        .input {
            width: 100%; padding: 12px 16px; border: 1px solid #e0e0e0;
            border-radius: 8px; font-size: 15px; outline: none;
        }
        .input:focus { border-color: #6750A4; }
        .card {
            background: #fff; border-radius: 16px; padding: 16px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
        }
        .nav-bar {
            position: fixed; bottom: 0; left: 50%; transform: translateX(-50%);
            width: 100%; max-width: 480px;
            display: flex; background: #fff; border-top: 1px solid #eee;
            padding: 8px 0; z-index: 100;
        }
        .nav-item {
            flex: 1; text-align: center; padding: 8px;
            cursor: pointer; color: #666; font-size: 11px;
        }
        .nav-item .icon { font-size: 22px; margin-bottom: 2px; }
        .search-bar {
            background: #F3EDF7; border-radius: 24px;
            padding: 10px 16px; display: flex; align-items: center;
            margin: 8px 16px;
        }
        .search-bar input {
            border: none; background: transparent; outline: none;
            flex: 1; margin-left: 8px; font-size: 14px;
        }
        .toolbar {
            background: #6750A4; color: #fff; padding: 16px;
            text-align: center; font-size: 18px; font-weight: 600;
            position: sticky; top: 0; z-index: 10;
        }
        .fab {
            position: fixed; bottom: 80px; right: 24px;
            width: 56px; height: 56px; border-radius: 50%;
            background: #6750A4; color: #fff; border: none;
            font-size: 24px; box-shadow: 0 4px 12px rgba(103,80,164,0.4);
            cursor: pointer; z-index: 99;
        }
        .divider { height: 1px; background: #e0e0e0; }
        .list-item {
            padding: 14px 16px; border-bottom: 1px solid #f0f0f0;
            display: flex; align-items: center;
        }
        .switch { position: relative; width: 48px; height: 28px; }
        .badge {
            display: inline-flex; align-items: center; justify-content: center;
            background: #BA1A1A; color: #fff; border-radius: 12px;
            padding: 2px 8px; font-size: 11px; font-weight: 600;
        }
        .tab-bar { display: flex; background: #6750A4; }
        .tab {
            flex: 1; text-align: center; padding: 12px;
            color: rgba(255,255,255,0.7); cursor: pointer; font-size: 14px;
        }
        .tab.active { color: #fff; border-bottom: 2px solid #fff; }
        .progress-bar { height: 4px; background: #e0e0e0; border-radius: 2px; overflow: hidden; }
        .progress-fill { height: 100%; background: #6750A4; transition: width 0.3s; }
        .slider { width: 100%; }
        .checkbox, .radio { display: flex; align-items: center; gap: 8px; padding: 8px; }
        .img-placeholder {
            background: #e0e0e0; display: flex; align-items: center;
            justify-content: center; color: #999; font-size: 48px;
            border-radius: 16px;
        }
        .webview-frame {
            border: 1px solid #e0e0e0; border-radius: 8px;
            background: #E3F2FD; display: flex; align-items: center;
            justify-content: center; color: #1565C0;
        }
    </style>
</head>
<body>
    <div class="app-container">
        $bodyElements
    </div>
    <script>
        // Navigation interaction
        document.querySelectorAll('.nav-item').forEach(item => {
            item.addEventListener('click', function() {
                document.querySelectorAll('.nav-item').forEach(i => i.style.color = '#666');
                this.style.color = '#6750A4';
            });
        });
        // Tab interaction
        document.querySelectorAll('.tab').forEach(tab => {
            tab.addEventListener('click', function() {
                document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
                this.classList.add('active');
            });
        });
        // Button ripple effect
        document.querySelectorAll('.btn').forEach(btn => {
            btn.addEventListener('click', function(e) {
                const rect = this.getBoundingClientRect();
                const ripple = document.createElement('span');
                ripple.style.cssText = 'position:absolute;border-radius:50%;background:rgba(255,255,255,0.4);' +
                    'width:100px;height:100px;left:'+(e.clientX-rect.left-50)+'px;top:'+(e.clientY-rect.top-50)+'px;' +
                    'transform:scale(0);animation:ripple 0.5s;pointer-events:none;';
                this.style.position = 'relative';
                this.style.overflow = 'hidden';
                this.appendChild(ripple);
                setTimeout(() => ripple.remove(), 500);
            });
        });
        const style = document.createElement('style');
        style.textContent = '@keyframes ripple{to{transform:scale(2.5);opacity:0;}}';
        document.head.appendChild(style);
    </script>
</body>
</html>"""
    }

    private fun buildHtmlElement(w: WidgetModel): String {
        val x = w.x.toInt()
        val y = w.y.toInt()
        val wd = w.width.toInt()
        val ht = w.height.toInt()
        val posStyle = "left:${x}px; top:${y}px; width:${wd}px; height:${ht}px;"

        return when (w.type) {
            WidgetModel.TYPE_BUTTON -> {
                val text = w.properties["text"] ?: "Button"
                val bg = w.properties["backgroundColor"] ?: "#6750A4"
                val tc = w.properties["textColor"] ?: "#FFFFFF"
                "<button class=\"widget btn\" style=\"$posStyle background:$bg; color:$tc;\">$text</button>"
            }
            WidgetModel.TYPE_TEXT -> {
                val text = w.properties["text"] ?: "Text"
                val color = w.properties["textColor"] ?: "#1C1B1F"
                val size = w.properties["textSize"] ?: 16
                "<div class=\"widget text\" style=\"$posStyle color:$color; font-size:${size}px;\">$text</div>"
            }
            WidgetModel.TYPE_IMAGE -> {
                "<div class=\"widget img-placeholder\" style=\"$posStyle\">🖼️</div>"
            }
            WidgetModel.TYPE_INPUT -> {
                val hint = w.properties["hint"] ?: "Enter text..."
                "<input class=\"widget input\" style=\"$posStyle\" placeholder=\"$hint\" />"
            }
            WidgetModel.TYPE_LIST -> {
                val count = (w.properties["itemCount"] as? Number)?.toInt() ?: 10
                val items = (1..count.coerceAtMost(8)).joinToString("\n            ") {
                    "<div class=\"list-item\">List Item $it</div>"
                }
                """<div class="widget" style="$posStyle overflow-y:auto;">
            $items
        </div>"""
            }
            WidgetModel.TYPE_NAV_BAR -> {
                val items = w.properties["items"] as? List<*> ?: listOf("Home", "Search", "Profile")
                val icons = listOf("🏠", "🔍", "👤", "⚙️", "🔔")
                val navItems = items.mapIndexed { i, item ->
                    "<div class=\"nav-item\"><div class=\"icon\">${icons[i % icons.size]}</div>$item</div>"
                }.joinToString("\n            ")
                """<div class="nav-bar">
            $navItems
        </div>"""
            }
            WidgetModel.TYPE_SEARCH_BAR -> {
                val hint = w.properties["hint"] ?: "Search..."
                """<div class="widget search-bar" style="$posStyle">
            <span>🔍</span>
            <input placeholder="$hint" />
        </div>"""
            }
            WidgetModel.TYPE_CARD -> {
                val title = w.properties["title"] ?: "Card Title"
                "<div class=\"widget card\" style=\"$posStyle\">$title</div>"
            }
            WidgetModel.TYPE_TOOLBAR -> {
                val title = w.properties["title"] ?: "Toolbar"
                "<div class=\"widget toolbar\" style=\"$posStyle\">$title</div>"
            }
            WidgetModel.TYPE_FAB -> {
                "<button class=\"fab widget\" style=\"$posStyle\">+</button>"
            }
            WidgetModel.TYPE_BADGE -> {
                val text = w.properties["text"] ?: "99+"
                "<span class=\"widget badge\" style=\"$posStyle\">$text</span>"
            }
            WidgetModel.TYPE_DIVIDER -> {
                "<div class=\"widget divider\" style=\"$posStyle\"></div>"
            }
            WidgetModel.TYPE_PROGRESS -> {
                val prog = (w.properties["progress"] as? Number)?.toInt() ?: 50
                """<div class="widget" style="$posStyle">
            <div class="progress-bar"><div class="progress-fill" style="width:${prog}%"></div></div>
        </div>"""
            }
            WidgetModel.TYPE_SWITCH -> {
                val text = w.properties["text"] ?: "Switch"
                """<label class="widget switch" style="$posStyle">
            <input type="checkbox" /> $text
        </label>"""
            }
            WidgetModel.TYPE_CHECKBOX -> {
                val text = w.properties["text"] ?: "Check me"
                """<label class="widget checkbox" style="$posStyle">
            <input type="checkbox" /> $text
        </label>"""
            }
            WidgetModel.TYPE_RADIO -> {
                val text = w.properties["text"] ?: "Option"
                """<label class="widget radio" style="$posStyle">
            <input type="radio" /> $text
        </label>"""
            }
            WidgetModel.TYPE_TAB -> {
                val tabs = w.properties["tabs"] as? List<*> ?: listOf("Tab1", "Tab2", "Tab3")
                val tabHtml = tabs.mapIndexed { i, t ->
                    "<div class=\"tab${if (i == 0) " active" else ""}\">$t</div>"
                }.joinToString("\n            ")
                """<div class="widget tab-bar" style="$posStyle">
            $tabHtml
        </div>"""
            }
            WidgetModel.TYPE_SLIDER -> {
                val val_ = (w.properties["value"] as? Number)?.toInt() ?: 50
                "<input type=\"range\" class=\"widget slider\" style=\"$posStyle\" value=\"$val_\" min=\"0\" max=\"100\" />"
            }
            WidgetModel.TYPE_WEBVIEW -> {
                val url = w.properties["url"] ?: ""
                "<div class=\"widget webview-frame\" style=\"$posStyle\">🌐 $url</div>"
            }
            else -> {
                "<div class=\"widget\" style=\"$posStyle; background:#eee; display:flex; align-items:center; justify-content:center; color:#999;\">${w.name}</div>"
            }
        }
    }
}
