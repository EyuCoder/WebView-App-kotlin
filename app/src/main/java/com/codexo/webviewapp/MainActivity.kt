package com.codexo.webviewapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
    private val mainUrl = "https://ebstv.tv/"
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var customDialog: Dialog

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.wv_main)
//        progressBar = findViewById(R.id.pb_dialog)
//        progressBar.max = 100
        swipeRefresh = findViewById(R.id.sr_main)

        customDialog = Dialog(this)
//        customDialog = Dialog(this, R.style.dialogTransparent)
        customDialog.setContentView(R.layout.dialog_lottie)
        customDialog.setCancelable(false)
        customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


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
//                    progressBar.progress = newProgress
//                    if (newProgress < 100 && progressBar.visibility == ProgressBar.GONE) {
                    if (newProgress < 100) {
                        customDialog.show()
//                        progressBar.visibility = ProgressBar.VISIBLE
                    }
                    if (newProgress == 100) {
                        customDialog.dismiss()
//                        progressBar.visibility = ProgressBar.GONE
                        swipeRefresh.isRefreshing = false
                    } else {
                        customDialog.show()
//                        progressBar.visibility = ProgressBar.VISIBLE
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
            val targetUrl1 = "www.ebstv.tv"
            val targetUrl2 = "ebstv.tv/"
            val targetUrl3 = ""
            if (Uri.parse(url).host == targetUrl1 || Uri.parse(url).host == targetUrl2) {
                webView.loadUrl(url)
                //Toast.makeText(baseContext, Uri.parse(url).host, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@MainActivity, url, Toast.LENGTH_LONG).show()
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
            return true
        }
    }
}
