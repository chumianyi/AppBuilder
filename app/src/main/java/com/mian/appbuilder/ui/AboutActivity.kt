package com.mian.appbuilder.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.mian.appbuilder.BuildConfig
import com.mian.appbuilder.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "About"

        val tv = findViewById<TextView>(R.id.aboutText)
        tv.text = """
AppBuilder v${BuildConfig.VERSION_NAME}

A drag-and-drop Android app builder with Material 3 and Morphicon icons.

=== Open Source Licenses ===

• Android Open Source Project (Apache 2.0)
• Material Components for Android (Apache 2.0)
• AndroidX Libraries (Apache 2.0)
• Room Database (Apache 2.0)
• Navigation Component (Apache 2.0)
• Lottie by Airbnb (Apache 2.0)
• Gson by Google (Apache 2.0)
• Kotlin Standard Library (Apache 2.0)
• Coroutines by JetBrains (Apache 2.0)
• Android-Iconics (Apache 2.0)
• ColorPickerView by skydoves (Apache 2.0)
• Markwon by noties (Apache 2.0)
• AndroidSVG by Paul Le Beault (Apache 2.0)

=== License ===

This application is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

GitHub: https://github.com/chumianyi/AppBuilder
        """.trimIndent()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
