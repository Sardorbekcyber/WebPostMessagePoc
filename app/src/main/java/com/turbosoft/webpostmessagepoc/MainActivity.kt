package com.turbosoft.webpostmessagepoc

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.web_custom)

        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = true
        webView.addJavascriptInterface(JsIntegration(), "Android")

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView, request: WebResourceRequest
            ): Boolean {
                view.loadUrl(request.url.toString())
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.d("IntegrationDebug", "OnPageStarted")

                webView.loadUrl(
                    "javascript:(function() {" +
                            "function receiveMessage(event) {\n" +
                            "Android.receiveMessage(JSON.stringify(event.data));\n" +
                            "}" +
                            "window.addEventListener(\"message\", receiveMessage, false);" +
                            "})()"
                );
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("IntegrationDebug", "OnPageCommitVisible")
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                Log.d("IntegrationDebug", "OnPageCommitVisible")
            }
        }

        val unencodedHtml =
            """
                <html>
                <body>

                <input type="button" value="Say hello" onClick="sendAndroidMessage('Hello Android!')"/>

                <script type="text/javascript">
                    function sendAndroidMessage(toast) {
                        Android.receiveMessage(toast);
                    }
                </script>

                </body>
                </html>
            """.trimIndent()
        val encodedHtml = Base64.encodeToString(unencodedHtml.toByteArray(), Base64.NO_PADDING)
        webView.loadData(encodedHtml, "text/html", "base64")
    }

}