package com.gx.smart.smartoa

import android.app.AlertDialog
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.Utils
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.push.PushMessageConstants.*
import com.gx.smart.webview.X5NetService
import com.jeremyliao.liveeventbus.LiveEventBus
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader


/**
 * @author xiaosy
 * @create 2019-11-01
 * @Describe
 */
class SmartOAApplication : Application() {

//    private lateinit var mJGPushReceiver: JGPushReceiver

    init {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater { context, layout ->
            layout.setPrimaryColorsId(
                R.color.background_style_four,
                R.color.font_color_style_six
            )//全局设置主题颜色
            ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate)//指定为经典Header，默认是 贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater { context, layout ->
            layout.setPrimaryColorsId(
                R.color.background_style_four,
                R.color.font_color_style_six
            )//全局设置主题颜色
            ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate)
        }
    }

    override fun onCreate() {
        super.onCreate()
        
        initLogger()
        Utils.init(this)
        preInitX5Core()
        Logger.d("SmartOAApplication initLogger")
        disableAPIDialog()
        initPush()
        CrashHandler.instance.init(this)
        initEventBus()
    }
    
    private fun initEventBus() {
        LiveEventBus
            .config()
            .enableLogger(true)
    }



    private fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)      //（可选）是否显示线程信息。 默认值为true
            .methodCount(2)               // （可选）要显示的方法行数。 默认2
            .methodOffset(7)               // （可选）设置调用堆栈的函数偏移值，0的话则从打印该Log的函数开始输出堆栈信息，默认是0
            .tag("SmartOA")                  //（可选）每个日志的全局标记。 默认PRETTY_LOGGER（如上图）
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    private fun preInitX5Core() {
        //预加载x5内核
        val intent = Intent(this, X5NetService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun initPush() {
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
        val jPushToken = JPushInterface.getRegistrationID(this)
        if (!TextUtils.isEmpty(jPushToken)) {
            AppConfig.JGToken = jPushToken
        }
        Logger.d("push", "jPushToken:$jPushToken")
//        registerJGPushBroadcast()
    }

    /**
     * 反射 禁止弹窗
     */
    private fun disableAPIDialog() {
        if (Build.VERSION.SDK_INT < 28) return
        try {
            val clazz = Class.forName("android.app.ActivityThread")
            val currentActivityThread =
                clazz.getDeclaredMethod("currentActivityThread")
            currentActivityThread.isAccessible = true
            val activityThread = currentActivityThread.invoke(null)
            val mHiddenApiWarningShown =
                clazz.getDeclaredField("mHiddenApiWarningShown")
            mHiddenApiWarningShown.isAccessible = true
            mHiddenApiWarningShown.setBoolean(activityThread, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//    //自定义接受房屋变化的广播接收器
//    internal class JGPushReceiver : BroadcastReceiver() {
//        private var dialog: AlertDialog? = null
//
//        override fun onReceive(
//            context: Context,
//            intent: Intent
//        ) {
//            when (intent.action) {
//                else -> {
//                    if (dialog != null && dialog!!.isShowing) {
//                        dialog?.dismiss()
//                    }
//                    dialog =
//                        AlertDialog.Builder(ActivityUtils.getTopActivity())
//                            .setTitle(AppConfig.JGPushContent).setMessage(AppConfig.JGPushMsg)
//                            .setCancelable(false)
//                            .setPositiveButton(
//                                "确定"
//                            ) { dialog, _ ->
//                                dialog.dismiss()
//                            }.show()
//                }
//            }
//        }
//    }

//    private fun registerJGPushBroadcast() {
//        val intentFilter = IntentFilter()
//        intentFilter.addAction(ACTION_JGPUSH_SMARTHOME)
//        intentFilter.addAction(ACTION_JGPUSH_SMARTVISITOR)
//        intentFilter.addAction(ACTION_JGPUSH_FAMILYCARE)
//        intentFilter.addAction(ACTION_JGPUSH_ADDFAMILYMEMBERS)
//        intentFilter.addAction(ACTION_JGPUSH_BINDINGHOUSE)
//        intentFilter.addAction(ACTION_JGPUSH_CHANGEPHONENUMBER)
//        intentFilter.addAction(ACTION_JGPUSH_HAWKEYEMONITORING)
//        intentFilter.addAction(ACTION_JGPUSH_DELETEFAMILYMEMBER)
//        mJGPushReceiver = JGPushReceiver()
//        registerReceiver(mJGPushReceiver, intentFilter)
//    }


}
