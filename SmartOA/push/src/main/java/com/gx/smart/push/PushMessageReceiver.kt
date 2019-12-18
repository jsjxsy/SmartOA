package com.gx.smart.push

import android.content.Context
import android.content.Intent
import android.util.Log
import cn.jpush.android.api.*
import cn.jpush.android.service.JPushMessageReceiver

class PushMessageReceiver : JPushMessageReceiver() {

    lateinit var mPushProxyCallBack: PushProxyCallBack
    override fun onMessage(context: Context?, customMessage: CustomMessage?) {
        Log.e(TAG, "[onMessage] " + customMessage!!)
        processCustomMessage(context, customMessage)
    }

    override fun onNotifyMessageOpened(context: Context?, message: NotificationMessage?) {
        Log.e(TAG, "[onNotifyMessageOpened] " + message!!)
        try {
            //打开自定义的Activity
            //            Intent i = new Intent(context, TestActivity.class);
            //            Bundle bundle = new Bundle();
            //            bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE,message.notificationTitle);
            //            bundle.putString(JPushInterface.EXTRA_ALERT,message.notificationContent);
            //            i.putExtras(bundle);
            //            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            //            context.startActivity(i);
        } catch (throwable: Throwable) {

        }

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
        mPushProxyCallBack.processPushMsg(context, customMessage?.message)
        mPushProxyCallBack.processPushMsg(context, customMessage?.message, customMessage?.extra)
        //        if (MainActivity.isForeground) {
        //            String message = customMessage.message;
        //            String extras = customMessage.extra;
        //            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
        //            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
        //            if (!ExampleUtil.isEmpty(extras)) {
        //                try {
        //                    JSONObject extraJson = new JSONObject(extras);
        //                    if (extraJson.length() > 0) {
        //                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
        //                    }
        //                } catch (JSONException e) {
        //
        //                }
        //
        //            }
        //            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
        //        }


    }

    companion object {
        private const val TAG = "PushMessageReceiver"
    }


}
