package com.gx.smart.smartoa.activity.ui.splash

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.gx.smart.lib.base.BaseFragment
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ActivityUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.WebViewActivity
import com.gx.smart.lib.http.api.AppFigureService
import com.gx.smart.lib.http.base.CallBack
import com.gx.wisestone.work.app.grpc.appfigure.ImagesResponse
import kotlinx.android.synthetic.main.splash_fragment.*
import java.util.*

class SplashFragment : BaseFragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private lateinit var viewModel: SplashViewModel
    private var mTime: TimeCount? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    fun showAd() {
        AppFigureService.getInstance().bannerFigure(object : CallBack<ImagesResponse?>() {
            override fun callBack(result: ImagesResponse?) {
                if (!ActivityUtils.isActivityAlive(activity)) {
                    return
                }
                if (result?.code == 100) {
                    val list = result.imagesInfoOrBuilderList
                    if (list.isNotEmpty()) {
                        val index = Random().nextInt(list.size)
                        val data = list[index]
                        val imageUrl = data.imageUrl + "?v=" + data.modifyTime

                        Glide.with(activity!!).load(imageUrl)
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    login()
                                    jumpButton.visibility = View.GONE
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    jumpButton.visibility = View.VISIBLE
                                    mTime = TimeCount(3000, 1000, jumpButton)
                                    mTime?.start()
                                    splashImage.setOnClickListener {
                                        mTime?.cancel()
                                        goWebView(list[index].forwardUrl)
                                    }
                                    jumpButton.setOnClickListener {
                                        mTime?.cancel()
                                        login()
                                    }
                                    return false
                                }

                            })
                            .into(splashImage)
                    } else {
                        login()
                    }
                } else {
                    login()
                }

            }

        })

    }


    private fun goWebView(url: String) {
        login()
        val intent = Intent(activity, WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.URL, url)
        ActivityUtils.startActivity(intent)
    }


    //广告展示定时器
    inner class TimeCount(
        millisInFuture: Long,
        countDownInterval: Long,
        private val jumpButton: Button
    ) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
            login()
        }

        override fun onTick(millisUntilFinished: Long) {
            jumpButton.text =
                String.format(
                    "%s",
                    millisUntilFinished.div(1000).toString() + "S"
                ) + "\n跳过"

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mTime?.cancel()
    }


    fun login() {
        (activity as SplashActivity).updateMessagePushAndLogin()
    }

}
