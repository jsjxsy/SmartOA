package com.gx.smart.module.login.base

import android.os.CountDownTimer
import android.widget.TextView
import com.gx.smart.lib.base.BaseFragment

/**
 *@author xiaosy
 *@create 2020/3/8
 *@Describe 封装获取验证码方法
 **/
open class BaseVerifyCodeFragment : BaseFragment() {
    var mTime: TimeCount? = null

    fun initTimer(verifyCodeText: TextView) {
        mTime = TimeCount(
            60000,
            1000,
            verifyCodeText
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mTime?.cancel()
    }

    //获取验证码定时器
    class TimeCount(
        millisInFuture: Long,
        countDownInterval: Long,
        private val verifyCodeText: TextView
    ) :
        CountDownTimer(millisInFuture, countDownInterval) {
        //时间到了
        override fun onFinish() {
            verifyCodeText.text = "获取验证码"
            verifyCodeText.isClickable = true
        }
        //时间进行中
        override fun onTick(millisUntilFinished: Long) {
            verifyCodeText.isClickable = false
            verifyCodeText.text = String.format(
                "%s",
                millisUntilFinished.div(1000).toString() + "s"
            )
        }
    }
}