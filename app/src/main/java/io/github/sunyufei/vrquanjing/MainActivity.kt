package io.github.sunyufei.vrquanjing

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : AppCompatActivity() {

    companion object {
        private val URL: String = "file:///android_asset/index.html"
    }

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.hide()

        webView = findViewById(R.id.webView)
        webView.loadUrl(URL)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                webView.loadUrl(url)
                return true
            }
        }

        var settings = webView.settings
        settings.javaScriptEnabled = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.allowFileAccess = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.loadsImagesAutomatically = true
        settings.defaultTextEncodingName = "UTF-8"
        settings.domStorageEnabled = true
        settings.allowUniversalAccessFromFileURLs = true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        } else
            return super.onKeyDown(keyCode, event)

    }
}
