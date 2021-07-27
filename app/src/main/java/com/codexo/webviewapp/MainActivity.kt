package com.codexo.webviewapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private val mainUrl = "https://ebstv.tv/"
    private var currentUrl = ""
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
        //swipeRefresh.isRefreshing = true

        swipeRefresh.setOnRefreshListener { webView.loadUrl(webView.url.toString()) }

        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState)
        } else {
            webView.settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
            }
            webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            webView.webChromeClient = MyWebChromeClient()
        }
    }

//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        // Check if the key event was the Back button and if there's history
//        if (keyCode == KeyEvent.KEYCODE_BACK && myWebView.canGoBack()) {
//            myWebView.goBack()
//            return true
//        }
//        // If it wasn't the Back key or there's no web page history, bubble up to the default
//        // system behavior (probably exit the activity)
//        return super.onKeyDown(keyCode, event)
//    }
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

    inner class MyWebChromeClient : WebChromeClient() {
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


        override fun onReceivedTitle(view: WebView, title: String) {

        }


    }

    inner class MyWVClient : WebViewClient() {
        val customHtml =
            """<html>
                |<body>
                |<div align=\"center\" >
                |<h1>
                |This is the description for the load fail : description \nThe failed url is : "$mainUrl"\n"
                |</h1>
                |</div>
                |</body>
                |</html>""".trimMargin()

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            val targetUrl1 = "www.ebstv.tv"
            val targetUrl2 = "ebstv.tv"
            val targetUrl3 = ""
            currentUrl = url
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

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
//            webView.loadUrl("about:blank")
//            val customView = layoutInflater.inflate(R.layout.error_view, view)
//            val errorText = customView.findViewById<TextView>(R.id.tv_error)
//            val retryBtn = customView.findViewById<Button>(R.id.btn_retry)
//            errorText.text = "failed to load page! \n Please check you connection and try again\n ${error.toString()}"
//            retryBtn.setOnClickListener { webView.loadUrl(currentUrl)}
//            webView.invalidate()

            Toast.makeText(applicationContext, "No internet connection", Toast.LENGTH_LONG).show()
            webView.loadUrl("file:///android_asset/error.html")

        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler,
            error: SslError?
        ) {
            super.onReceivedSslError(view, handler, error)
            handler.cancel()
        }
    }
}
