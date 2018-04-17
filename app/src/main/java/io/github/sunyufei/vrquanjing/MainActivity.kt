package io.github.sunyufei.vrquanjing

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    companion object {
        private const val URL: String = "file:///android_asset/index.html"
    }

    private lateinit var webView: WebView
    private var backPressed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.hide()

        webView = findViewById(R.id.webView)

        val settings = webView.settings
        settings.allowFileAccess = true
        settings.allowUniversalAccessFromFileURLs = true
        settings.defaultTextEncodingName = "UTF-8"
        settings.domStorageEnabled = true
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.loadWithOverviewMode = true
        settings.loadsImagesAutomatically = true
        settings.useWideViewPort = true

        webView.loadUrl(URL)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                webView.loadUrl(url)
                return true
            }
        }
    }

    override fun onBackPressed() {
        when (webView.canGoBack()) {
            true -> {
                webView.goBack()
                backPressed = false
            }
            false -> {
                when (backPressed) {
                    true -> super.onBackPressed()
                    false -> {
                        backPressed = true
                        Toast.makeText(this@MainActivity, "再按一下退出", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
