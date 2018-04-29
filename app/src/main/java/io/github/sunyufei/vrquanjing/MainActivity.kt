package io.github.sunyufei.vrquanjing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {

    companion object {
        private const val URL: String = "file:///android_asset/index.html"
        private const val BASE_URL: String = "https://sunovo.gitee.io/quanjing/"
    }

    private var backPressed: Boolean = false

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.hide()

        webView = findViewById(R.id.webView)

        webView.run {

            settings.run {
                allowFileAccess = true
                allowUniversalAccessFromFileURLs = true
                cacheMode = WebSettings.LOAD_DEFAULT
                defaultTextEncodingName = "UTF-8"
                domStorageEnabled = true
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                loadWithOverviewMode = true
                loadsImagesAutomatically = true
                useWideViewPort = true
            }

            loadUrl(URL)

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    loadUrl(url)
                    return true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    when (url == URL) {
                        true -> this@MainActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                        false -> this@MainActivity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    }
                    super.onPageFinished(view, url)
                }
            }

            setOnLongClickListener {
                when (url == URL) {
                    false -> {
                        val localUrl: String = url
                        val index = localUrl.indexOf("file:///android_asset/") + "file:///android_asset/".length
                        val newUrl = BASE_URL + localUrl.substring(index)
                        showQRCode(newUrl)
                    }
                }
                true
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
                        toast("再按一下退出")
                    }
                }
            }
        }
    }


    private fun showQRCode(url: String?) {
        val imageView = ImageView(this@MainActivity)
        val qrCodeWriter = QRCodeWriter()

        val windowManager = this@MainActivity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val size = when (displayMetrics.widthPixels > displayMetrics.heightPixels) {
            true -> (displayMetrics.heightPixels * 0.75).toInt()
            false -> (displayMetrics.widthPixels * 0.75).toInt()
        }

        val bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, size, size)

        val pixels = IntArray(size * size)
        for (i in 0 until size)
            for (j in 0 until size) when (bitMatrix.get(i, j)) {
                true -> pixels[i * size + j] = Color.BLACK
                false -> pixels[i * size + j] = Color.WHITE
            }

        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, size, 0, 0, size, size)

        imageView.setImageBitmap(bitmap)

        val builder = AlertDialog.Builder(this@MainActivity)
        builder.run {
            setTitle("分享")
            setView(imageView)
            setPositiveButton("关闭", null)
            show()
        }
    }
}
