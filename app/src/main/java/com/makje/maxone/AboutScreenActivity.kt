package com.makje.maxone

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView

class AboutScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_screen)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val textView = findViewById(R.id.about_screen_content) as WebView
        textView.loadData(getString(R.string.about_screen_content), "text/html; charset=utf-8", "UTF-8")
    }
}
