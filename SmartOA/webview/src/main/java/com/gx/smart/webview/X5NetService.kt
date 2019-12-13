package com.gx.smart.webview

import android.app.IntentService
import android.content.Intent
import android.util.Log

import com.tencent.smtt.sdk.QbSdk

/**
 * Created by tzw on 2018/3/14.
 */

class X5NetService : IntentService {

    private var cb: QbSdk.PreInitCallback = object : QbSdk.PreInitCallback {
        override fun onViewInitFinished(success: Boolean) {
            //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动
            if(success){
                Log.e(TAG,"x5 初始化成功")
            }else{
                Log.d(TAG,"x5 初始化失败")
            }

        }

        override fun onCoreInitFinished() {
            // TODO Auto-generated method stub
        }
    }

    constructor() : super(TAG) {}
    constructor(name: String) : super(TAG) {}

    public override fun onHandleIntent(intent: Intent?) {
        Log.e("--->","onHandleIntent")
        initX5Web()
    }

    private fun initX5Web() {
        if (!QbSdk.isTbsCoreInited()) {
            // 设置X5初始化完成的回调接口
            QbSdk.preInit(applicationContext, null)
        }
        QbSdk.initX5Environment(applicationContext, cb)
    }

    companion object {
        private val TAG = X5NetService::class.java.simpleName
    }


}



