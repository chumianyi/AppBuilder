package com.mian.appbuilder.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.mian.appbuilder.R

class TemplateDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template_detail)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val name = intent.getStringExtra("template_name") ?: "Template"
        supportActionBar?.title = name

        findViewById<TextView>(R.id.detailText).text =
            "Template: $name\n\nThis template is ready to use.\nTap to apply to canvas."
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
