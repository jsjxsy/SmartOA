package com.gx.smart.smartoa.push

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import cn.jpush.android.api.*
import cn.jpush.android.service.JPushMessageReceiver
import com.gx.smart.smartoa.R
import com.orhanobut.logger.Logger

class PushMessageReceiver : JPushMessageReceiver() {


    override fun onMessage(context: Context?, customMessage: CustomMessage?) {
        Log.e(TAG, "[onMessage] " + customMessage!!)
        processCustomMessage(context, customMessage)
    }

    override fun onNotifyMessageOpened(context: Context?, message: NotificationMessage?) {
        Log.e(TAG, "[onNotifyMessageOpened] " + message!!)


    }

    override fun onMultiActionClicked(context: Context, intent: Intent) {
        Log.e(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮")
        val nActionExtra = intent.extras!!.getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA)

        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
        if (nActionExtra == null) {
            Log.d(TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null")
            return
        }
        if (nActionExtra == "my_extra1") {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮一")
        } else if (nActionExtra == "my_extra2") {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮二")
        } else if (nActionExtra == "my_extra3") {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮三")
        } else {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮未定义")
        }
    }

    override fun onNotifyMessageArrived(context: Context?, message: NotificationMessage?) {
        Log.e(TAG, "[onNotifyMessageArrived] " + message!!)
        try {

            val mNotificationManager =
                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder1 =
                NotificationCompat.Builder(context, "default")
            builder1.setSmallIcon(R.mipmap.ic_launcher) //设置图标

            //builder1.setTicker("显示第二个通知");
            builder1.setContentTitle(message.notificationTitle)
            builder1.setContentText(message.notificationContent) //消息内容

            builder1.setWhen(System.currentTimeMillis()) //发送时间
            builder1.setDefaults(Notification.DEFAULT_ALL) //设置默认的提示音，振动方式，灯光
            builder1.setAutoCancel(true) //打开程序后图标消失
            builder1.priority = NotificationCompat.PRIORITY_MAX
            val intent = Intent(Intent.ACTION_MAIN)
            intent.setClass(context, context.javaClass)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            //当任务栏上该通知被点击时执行的页面跳转
            val pIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            builder1.setContentIntent(pIntent)
            mNotificationManager.notify(0, builder1.build())
        } catch (e: Throwable) {
            Logger.e(TAG, e.printStackTrace())
        }
    }

    override fun onNotifyMessageDismiss(context: Context?, message: NotificationMessage?) {
        Log.e(TAG, "[onNotifyMessageDismiss] " + message!!)
    }

    override fun onRegister(context: Context?, registrationId: String?) {
        Log.e(TAG, "[onRegister] " + registrationId!!)
    }

    override fun onConnected(context: Context?, isConnected: Boolean) {
        Log.e(TAG, "[onConnected] $isConnected")
    }

    override fun onCommandResult(context: Context?, cmdMessage: CmdMessage?) {
        Log.e(TAG, "[onCommandResult] " + cmdMessage!!)
    }

    override fun onTagOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        //        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context,jPushMessage);
        super.onTagOperatorResult(context, jPushMessage)
    }

    override fun onCheckTagOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        //        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context,jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage)
    }

    override fun onAliasOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        //        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context,jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage)
    }

    override fun onMobileNumberOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        //        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context,jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage)
    }

    //send msg to MainActivity
    private fun processCustomMessage(context: Context?, customMessage: CustomMessage?) {

        val mNotificationManager =
            context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder1 = NotificationCompat.Builder(context, NotificationChannel.DEFAULT_CHANNEL_ID)
        builder1.setSmallIcon(R.mipmap.ic_launcher) //设置图标

        builder1.setContentTitle("智慧办公") //设置标题

        builder1.setContentText(customMessage?.message) //消息内容

        builder1.setWhen(System.currentTimeMillis()) //发送时间

        builder1.setDefaults(Notification.DEFAULT_ALL) //设置默认的提示音，振动方式，灯光
        builder1.priority = NotificationCompat.PRIORITY_MAX
        builder1.setAutoCancel(true) //打开程序后图标消失

        val intent1 = Intent(Intent.ACTION_MAIN)
        intent1.setClass(context, context.javaClass)
        //当任务栏上该通知被点击时执行的页面跳转
        val pintent = PendingIntent.getActivity(
            context, SystemClock.uptimeMillis().toInt(), intent1,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder1.setContentIntent(pintent)
        val notify = builder1.build()
        mNotificationManager.notify(1, notify)


    }

    companion object {
        private const val TAG = "PushMessageReceiver"
    }


}
