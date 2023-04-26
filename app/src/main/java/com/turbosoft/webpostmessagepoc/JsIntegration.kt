package com.turbosoft.webpostmessagepoc

import android.util.Log
import android.webkit.JavascriptInterface

class JsIntegration {

    @JavascriptInterface
    fun receiveMessage(data: String): Boolean {
        Log.d("IntegrationDebug", "Data from JS -> $data")
        return true
    }

}