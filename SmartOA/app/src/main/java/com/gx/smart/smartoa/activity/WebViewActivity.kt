package com.gx.smart.smartoa.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.activity_webview_layout.*
import kotlinx.android.synthetic.main.layout_common_title.*

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_layout)
        initTitle()
        initContent()
    }


    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener {
                if (fullWebView.canGoBack()) {
                    fullWebView.goBack()
                } else {
                    onBackPressed()
                }
            }
        }

    }

    private fun initContent() {
        val url = intent.getStringExtra(URL)
        fullWebView.loadUrl(url)
        center_title.let {
            it.visibility = View.VISIBLE
            it.text = fullWebView.title
        }
    }

    companion object {
        val URL = "URL"
    }
}
