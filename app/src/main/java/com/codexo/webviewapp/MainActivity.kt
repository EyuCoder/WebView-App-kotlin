package com.codexo.webviewapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : AppCompatActivity() {
    lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.wv_main)
        webView.webViewClient = WebViewClient()

        webView.settings.apply {
            javaScriptEnabled
            cacheMode
        }
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.loadUrl("https://zenachmart.com")
    }

    override fun onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack()
        } else super.onBackPressed()
    }
}