package com.gx.smart.smartoa

import android.app.Application
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ProcessUtils
import com.blankj.utilcode.util.Utils
import com.gx.smart.common.AppConfig
import com.gx.smart.webview.X5NetService
import com.jeremyliao.liveeventbus.LiveEventBus
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import org.litepal.LitePal


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
        if (ProcessUtils.isMainProcess()) {
            AppUtils.getAppPackageName()
            initLogger()
            Utils.init(this)
            preInitX5Core()
            disableAPIDialog()
            initPush()
            CrashHandler.instance.init(this)
            initEventBus()
            initDataBase()
            initARouter()
        }
    }

    private fun initDataBase() {
        LitePal.initialize(this)
    }

    private fun initEventBus() {
        LiveEventBus
            .config()
            .enableLogger(true)
    }


    private fun initLogger() {
        Logger.d("SmartOAApplication initLogger")
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

    /**
     * 初始化 ARouter
     */
    private fun initARouter() {
        // These two lines must be written before init, otherwise these configurations will be invalid in the init process
        if (BuildConfig.DEBUG) {
            ARouter.openLog()     // Print log
            ARouter.openDebug()   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
        }
        ARouter.init(this)
    }


}
