package com.gx.smart.smartoa

import android.content.Context
import android.os.Environment
import android.os.Process
import android.util.Log
import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author xiaosy
 * @create 2019-11-07
 * @Describe
 */
class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null
    //用来存储设备信息和异常信息
    private val infos: Map<String, String> =
        HashMap()
    //用于格式化日期,作为日志文件名的一部分
    private val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
    // 程序的Context对象
    private var mContext: Context? = null

    /**
     * 初始化
     *
     * @param context
     */
    fun init(context: Context?) {
        mContext = context
        // 获取系统默认的UncaughtException处理
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        // 设置该CrashHandler为程序的默认处理
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 异常捕获
     */
    override fun uncaughtException(
        thread: Thread,
        ex: Throwable
    ) { // 提示信息
        if (!handleException(ex) && mDefaultHandler != null) { // 如果用户没有处理则让系统默认的异常处理器来处
            mDefaultHandler!!.uncaughtException(thread, ex)
        }
        appExit()
    }

    /**
     * 自定义错误捕获
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            return false
        }
        // 收集错误信息
        saveCrashInfo2File(ex)
        return true
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private fun saveCrashInfo2File(ex: Throwable): String? {
        val sb = StringBuffer()
        for ((key, value) in infos) {
            sb.append("$key=$value\n")
        }
        val writer: Writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        sb.append(result)
        try {
            val timestamp = System.currentTimeMillis()
            val time = formatter.format(Date())
            val fileName = "crash-$time-$timestamp.log"
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                val path = "/sdcard/crash/"
                val dir = File(path)
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                val fos = FileOutputStream(path + fileName)
                fos.write(sb.toString().toByteArray())
                fos.close()
            }
            return fileName
        } catch (e: Exception) {
            Log.e(
                TAG,
                "an error occured while writing file...",
                e
            )
        }
        return null
    }

    /**
     * 退出应用程序
     */
    private fun appExit() {
        try {
            // 杀死该应用进程
            Process.killProcess(Process.myPid())
            System.exit(0)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        val TAG:String = CrashHandler::class.java.simpleName
        // CrashHandler实例
        val instance = CrashHandler()

    }
}