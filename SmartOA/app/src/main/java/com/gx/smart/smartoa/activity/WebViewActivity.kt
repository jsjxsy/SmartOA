package com.gx.smart.smartoa.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.base.BaseActivity
import com.gx.smart.webview.X5WebView
import kotlinx.android.synthetic.main.activity_webview_layout.*
import kotlinx.android.synthetic.main.layout_common_title.*


class WebViewActivity : BaseActivity() {

    private var mWebView: X5WebView? = null
    private lateinit var mViewParent: ViewGroup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_layout)
        init()
        initTitle()
        initContent()
    }


    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener {
                if (mWebView!!.canGoBack()) {
                    mWebView?.goBack()
                } else {
                    onBackPressed()
                }
            }
        }

    }

    private fun initContent() {
        val url = intent.getStringExtra(URL)
        mWebView!!.loadUrl(url)
        center_title.let {
            it.visibility = View.VISIBLE
            mWebView?.setTitle(center_title)
        }
    }

    private fun init() {
        mWebView = X5WebView(this, null)

        mViewParent = fullWebView
        mViewParent.addView(
            mWebView, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

    }

    override fun onDestroy() {
        //销毁时候需要处理Webview移除
        if (mWebView != null && mWebView!!.parent != null) {
            (mWebView!!.parent as ViewGroup).removeView(mWebView)
            mWebView!!.destroy()
            mWebView = null
            val view: ViewGroup = window.decorView as ViewGroup
            view.removeAllViews()
        }
        super.onDestroy()
    }

    companion object {
        const val URL = "URL"
    }

}
