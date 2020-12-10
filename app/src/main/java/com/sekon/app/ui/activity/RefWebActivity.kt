package com.sekon.app.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.sekon.app.R
import kotlinx.android.synthetic.main.activity_ref_web.*


class RefWebActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ref_web)

        pb_wv_ref.isVisible = true

        supportActionBar?.hide()
        actionBar?.hide()

        val url = intent.getStringExtra("url")
        val name = intent.getStringExtra("name")

        wv_referensi.settings.javaScriptEnabled = true

        wv_referensi.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                pb_wv_ref.isVisible = false
                view.loadUrl("javascript:alert('Web $name berhasil dimuat')")
            }
        }

        wv_referensi.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView, url: String, message: String, result: android.webkit.JsResult): Boolean {
                Toast.makeText(this@RefWebActivity, message, Toast.LENGTH_LONG).show()
                result.confirm()
                return true
            }
        }

        if (url != null) {
            wv_referensi.loadUrl(url.toString())
        }

    }
}