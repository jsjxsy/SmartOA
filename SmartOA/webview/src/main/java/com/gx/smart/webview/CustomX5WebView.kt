package com.gx.smart.webview

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.CookieManager
import com.tencent.smtt.sdk.CookieSyncManager
import com.tencent.smtt.sdk.DownloadListener
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

import java.util.ArrayList

class CustomX5WebView : WebView {

    internal var progressBar: ProgressBar? = null
    private val tvTitle: TextView? = null
    private val imageView: ImageView? = null
    private var newList: MutableList<String>? = null

    private val chromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            if (tvTitle == null || TextUtils.isEmpty(title)) {
                return
            }
            if (title != null && title.length > MAX_LENGTH) {
                tvTitle.text = title.subSequence(0, MAX_LENGTH).toString() + "..."
            } else {
                tvTitle.text = title
            }
        }

        //监听进度
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            progressBar!!.progress = newProgress
            if (progressBar != null && newProgress != 100) {

                //Webview加载没有完成 就显示我们自定义的加载图
                progressBar!!.visibility = View.VISIBLE

            } else if (progressBar != null) {

                //Webview加载完成 就隐藏进度条,显示Webview
                progressBar!!.visibility = View.GONE
                imageView!!.visibility = View.GONE
            }
        }
    }

    private val client = object : WebViewClient() {

        //当页面加载完成的时候
        override fun onPageFinished(webView: WebView?, url: String?) {
            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            val endCookie = cookieManager.getCookie(url)
            Log.i(TAG, "onPageFinished: endCookie : $endCookie")
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.getInstance().sync()//同步cookie
            } else {
                CookieManager.getInstance().flush()
            }
            super.onPageFinished(webView, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
            //返回值是true的时候控制去WebView打开，
            // 为false调用系统浏览器或第三方浏览器
            if (url.startsWith("http") || url.startsWith("https") || url.startsWith("ftp")) {
                return false
            } else {
                try {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse(url)
                    view!!.context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(view!!.context, "手机还没有安装支持打开此网页的应用！", Toast.LENGTH_SHORT).show()
                }

                return true
            }
        }

        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse {
            return super.shouldInterceptRequest(view, request)
        }

        override fun onLoadResource(webView: WebView?, s: String?) {
            super.onLoadResource(webView, s)
            val reUrl = webView!!.url + ""
            //            Log.i(TAG, "onLoadResource: onLoadResource : " + reUrl);
            val urlList = ArrayList<String>()
            urlList.add(reUrl)
            newList = ArrayList()
            for (cd in urlList) {
                if (!newList!!.contains(cd)) {
                    newList!!.add(cd)
                }
            }
        }


    }

    internal var downloadListener: DownloadListener =
        DownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        }


    constructor(context: Context) : super(context) {
        initUI()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initUI()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initUI()
    }

    fun setShowProgress(showProgress: Boolean) {
        if (showProgress) {
            progressBar!!.visibility = View.VISIBLE
        } else {
            progressBar!!.visibility = View.GONE
        }
    }


    private fun initUI() {

        x5WebViewExtension.setScrollBarFadingEnabled(false)
        isHorizontalScrollBarEnabled = false//水平不显示小方块
        isVerticalScrollBarEnabled = false //垂直不显示小方块

        //      setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);//滚动条在WebView内侧显示
        //      setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条在WebView外侧显示


        //        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        //        progressBar.setMax(100);
        //        progressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.color_progressbar));
        //
        //        addView(progressBar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 6));
        //        imageView = new ImageView(getContext());
        //        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ////      加载图 根据自己的需求去集成使用
        //        imageView.setImageResource(R.mipmap.splash);
        //        imageView.setVisibility(VISIBLE);
        //        addView(imageView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        initWebViewSettings()
    }

    //   基本的WebViewSetting
    fun initWebViewSettings() {
        setBackgroundColor(resources.getColor(android.R.color.white))
        webViewClient = client
        webChromeClient = chromeClient
        setDownloadListener(downloadListener)
        isClickable = true
        setOnTouchListener { v, event -> false }
        val webSetting = settings
        webSetting.javaScriptEnabled = true
        webSetting.builtInZoomControls = true
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        webSetting.domStorageEnabled = true
        webSetting.allowFileAccess = true
        webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSetting.setSupportZoom(true)
        webSetting.useWideViewPort = true
        webSetting.setSupportMultipleWindows(true)
        webSetting.setAppCacheEnabled(true)
        webSetting.setGeolocationEnabled(true)
        webSetting.setAppCacheMaxSize(java.lang.Long.MAX_VALUE)
        webSetting.pluginState = WebSettings.PluginState.ON_DEMAND
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH)
        //android 默认是可以打开_bank的，是因为它默认设置了WebSettings.setSupportMultipleWindows(false)
        //在false状态下，_bank也会在当前页面打开……
        //而x5浏览器，默认开启了WebSettings.setSupportMultipleWindows(true)，
        // 所以打不开……主动设置成false就可以打开了
        //需要支持多窗体还需要重写WebChromeClient.onCreateWindow
        webSetting.setSupportMultipleWindows(false)
        //        webSetting.setCacheMode(WebSettings.LOAD_NORMAL);
        //        getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && this.canGoBack()) {
            this.goBack() // goBack()表示返回WebView的上一页面
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun syncCookie(url: String, cookie: String) {
        CookieSyncManager.createInstance(context)
        if (!TextUtils.isEmpty(url)) {
            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            cookieManager.removeSessionCookie()// 移除
            cookieManager.removeAllCookie()

            //这里的拼接方式是伪代码
            val split = cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (string in split) {
                //为url设置cookie
                // ajax方式下  cookie后面的分号会丢失
                cookieManager.setCookie(url, string)
            }
            val newCookie = cookieManager.getCookie(url)
            Log.i(TAG, "syncCookie: newCookie == $newCookie")
            //sdk21之后CookieSyncManager被抛弃了，换成了CookieManager来进行管理。
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.getInstance().sync()//同步cookie
            } else {
                CookieManager.getInstance().flush()
            }
        } else {

        }

    }

    //删除Cookie
    private fun removeCookie() {

        CookieSyncManager.createInstance(context)
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeSessionCookie()
        cookieManager.removeAllCookie()
        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync()
        } else {
            CookieManager.getInstance().flush()
        }

    }

    fun getDoMain(url: String): String {
        var domain = ""
        val start = url.indexOf(".")
        if (start >= 0) {
            val end = url.indexOf("/", start)
            if (end < 0) {
                domain = url.substring(start)
            } else {
                domain = url.substring(start, end)
            }
        }
        return domain
    }

    companion object {

        private val TAG = CustomX5WebView::class.java.simpleName
        private val MAX_LENGTH = 8
    }
}
