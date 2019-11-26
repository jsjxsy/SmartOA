package com.gx.smart.smartoa

import android.app.Application
import android.content.Intent
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.Utils
import com.gx.smart.webview.X5NetService
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

    init {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater { context, layout ->
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white)//全局设置主题颜色
            ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate)//指定为经典Header，默认是 贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater { context, _ ->
            ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate)

        }
    }

    override fun onCreate() {
        super.onCreate()
        initLogger()
        Utils.init(this)
        preInitX5Core()
        Logger.d("SmartOAApplication initLogger")
        initPush()
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
        startService(intent)
    }

    private fun initPush() {
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
    }


}
