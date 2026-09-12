package com.mian.appbuilder.codegen

import android.view.View
import com.mian.appbuilder.model.WidgetModel

/**
 * Generates Kotlin Android code with full build.gradle from designer canvas.
 */
object KotlinGenerator {

    private fun Int.toDp(): String = "($this * resources.displayMetrics.density).toInt()"

    fun generateActivity(widgets: List<WidgetModel>, projectName: String): String {
        val packageName = "com.generated.${projectName.lowercase().replace(" ", "")}"

        val widgetInit = widgets.joinToString("\n        ") { generateWidgetInit(it) }

        return """package $packageName

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootLayout = ConstraintLayout(this).apply {
            setBackgroundColor(android.graphics.Color.WHITE)
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        }
        setContentView(rootLayout)

        $widgetInit
    }
}
"""
    }

    private fun generateWidgetInit(w: WidgetModel): String {
        val id = "widget_${w.id.take(8)}"
        val wDp = w.width.toInt().toDp()
        val hDp = w.height.toInt().toDp()
        val xDp = w.x.toInt().toDp()
        val yDp = w.y.toInt().toDp()

        return when (w.type) {
            WidgetModel.TYPE_BUTTON -> {
                val text = (w.properties["text"] ?: "Button").toString()
                val bg = (w.properties["backgroundColor"] ?: "#6750A4").toString()
                """val $id = Button(this).apply {
            text = "$text"
            setBackgroundColor(android.graphics.Color.parseColor("$bg"))
            setTextColor(android.graphics.Color.WHITE)
            isAllCaps = false
            setOnClickListener { /* TODO: Add click action */ }
        }
        rootLayout.addView($id, ConstraintLayout.LayoutParams($wDp, $hDp).apply {
            leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            marginStart = $xDp
            topMargin = $yDp
        })"""
            }
            WidgetModel.TYPE_TEXT -> {
                val text = (w.properties["text"] ?: "Text").toString()
                val color = (w.properties["textColor"] ?: "#1C1B1F").toString()
                val size = (w.properties["textSize"] as? Number)?.toFloat() ?: 16f
                """val $id = TextView(this).apply {
            text = "$text"
            setTextColor(android.graphics.Color.parseColor("$color"))
            textSize = $size
        }
        rootLayout.addView($id, ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            marginStart = $xDp
            topMargin = $yDp
        })"""
            }
            WidgetModel.TYPE_INPUT -> {
                val hint = (w.properties["hint"] ?: "Enter text...").toString()
                """val $id = EditText(this).apply {
            hint = "$hint"
            setPadding(24, 24, 24, 24)
        }
        rootLayout.addView($id, ConstraintLayout.LayoutParams($wDp, $hDp).apply {
            leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            marginStart = $xDp
            topMargin = $yDp
        })"""
            }
            WidgetModel.TYPE_CARD -> {
                val title = (w.properties["title"] ?: "Card Title").toString()
                """val $id = MaterialCardView(this).apply {
            radius = 16f
            cardElevation = 4f
            addView(TextView(this@MainActivity).apply {
                text = "$title"
                setPadding(32, 32, 32, 32)
            })
        }
        rootLayout.addView($id, ConstraintLayout.LayoutParams($wDp, $hDp).apply {
            leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            marginStart = $xDp
            topMargin = $yDp
        })"""
            }
            WidgetModel.TYPE_FAB -> {
                """val $id = FloatingActionButton(this).apply {
            setImageResource(android.R.drawable.ic_input_add)
            setOnClickListener { /* TODO */ }
        }
        rootLayout.addView($id)"""
            }
            WidgetModel.TYPE_NAV_BAR -> {
                """val $id = BottomNavigationView(this).apply {
            setOnItemSelectedListener { item -> true }
        }
        rootLayout.addView($id, ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        })"""
            }
            else -> {
                """// ${w.name} (${w.type})
        val $id = View(this).apply {
            setBackgroundColor(0xFFEEEEEE.toInt())
        }
        rootLayout.addView($id, ConstraintLayout.LayoutParams($wDp, $hDp).apply {
            leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            marginStart = $xDp
            topMargin = $yDp
        })"""
            }
        }
    }

    fun generateBuildGradle(appName: String, packageName: String): String {
        return """plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
}

android {
    namespace '$packageName'
    compileSdk 34

    defaultConfig {
        applicationId "$packageName"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = '17'
    }

    buildFeatures {
        viewBinding true
    }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.12.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
}
"""
    }

    fun generateAndroidManifest(packageName: String): String {
        return """<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.Material3.DayNight">
        <activity android:name=".MainActivity" android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
"""
    }

    fun generateSettingsGradle(appName: String): String {
        return """pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "$appName"
include ':app'
"""
    }

    fun generateRootBuildGradle(): String {
        return """plugins {
    id 'com.android.application' version '8.2.2' apply false
    id 'org.jetbrains.kotlin.android' version '1.9.22' apply false
}
"""
    }
}
