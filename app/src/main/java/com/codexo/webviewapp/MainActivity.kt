package com.codexo.webviewapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.wv_main)
        progressBar = findViewById(R.id.pb_main)
        progressBar.max = 100
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://zenachmart.com")

        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState)
        } else {
            webView.settings.apply {
                javaScriptEnabled = true
                cacheMode
            }
            webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

            webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    progressBar.progress = newProgress
                    if (newProgress < 100 && progressBar.visibility == ProgressBar.GONE) {
                        progressBar.visibility = ProgressBar.VISIBLE
                    }
                    if (newProgress == 100) {
                        progressBar.visibility = ProgressBar.GONE
                    } else {
                        progressBar.visibility = ProgressBar.VISIBLE
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else super.onBackPressed()
    }

    inner class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            val targetUrl = "https//www.somelinksaddress.com/me/you"
            if (targetUrl == url) {
                // Start your activity here
            } else {
                view.loadUrl(url)
            }
            return true
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            progressBar.visibility = View.VISIBLE
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView, url: String) {
            progressBar.visibility = View.GONE
            super.onPageFinished(view, url)
        }
    }
}
