package com.codexo.webviewapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private val mainUrl = "https://zenachmart.com"
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.wv_main)
        progressBar = findViewById(R.id.pb_main)
        swipeRefresh = findViewById(R.id.sr_main)
        progressBar.max = 100

        webView.webViewClient = MyWVClient()
        webView.loadUrl(mainUrl)
        swipeRefresh.isRefreshing = true

        swipeRefresh.setOnRefreshListener { webView.loadUrl(webView.url.toString()) }

        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState)
        } else {
            webView.settings.apply {
                javaScriptEnabled = true
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
                        swipeRefresh.isRefreshing = false
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
        } else {
            Snackbar.make(webView, "Are you sure you want to exit?", Snackbar.LENGTH_LONG)
                .setAction("Exit!") {
                    super.onBackPressed()
                }.show()
        }
    }

    inner class MyWVClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            val targetUrl = "zenachmart.com"
            if (Uri.parse(url).host == targetUrl) {
                webView.loadUrl(url)
                Toast.makeText(baseContext, Uri.parse(url).host, Toast.LENGTH_LONG).show()
            } else {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
            return true
        }
    }
}
