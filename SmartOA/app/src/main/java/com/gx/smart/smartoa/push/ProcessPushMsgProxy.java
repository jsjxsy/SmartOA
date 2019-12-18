package com.gx.smart.smartoa.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.SPUtils;
import com.gx.smart.smartoa.R;
import com.gx.smart.smartoa.data.network.AppConfig;
import com.orhanobut.logger.Logger;

import static com.gx.smart.smartoa.push.PushMessageConstants.ACTION_JGPUSH_ACTIVITYRECOMMENDATION;
import static com.gx.smart.smartoa.push.PushMessageConstants.ACTION_JGPUSH_ADDFAMILYMEMBERS;
import static com.gx.smart.smartoa.push.PushMessageConstants.ACTION_JGPUSH_BINDINGHOUSE;
import static com.gx.smart.smartoa.push.PushMessageConstants.ACTION_JGPUSH_CHANGEPHONENUMBER;
import static com.gx.smart.smartoa.push.PushMessageConstants.ACTION_JGPUSH_DELETEFAMILYMEMBER;
import static com.gx.smart.smartoa.push.PushMessageConstants.ACTION_JGPUSH_FAMILYCARE;
import static com.gx.smart.smartoa.push.PushMessageConstants.ACTION_JGPUSH_HAWKEYEMONITORING;
import static com.gx.smart.smartoa.push.PushMessageConstants.ACTION_JGPUSH_PROPERTYANNOUNCEMENT;
import static com.gx.smart.smartoa.push.PushMessageConstants.ACTION_JGPUSH_REMOTELOGIN;
import static com.gx.smart.smartoa.push.PushMessageConstants.ACTION_JGPUSH_SMARTHOME;
import static com.gx.smart.smartoa.push.PushMessageConstants.ACTION_JGPUSH_SMARTVISITOR;

//import com.gViewerX.util.LogUtils;
//import com.jrj.smartHome.AppConfig;
//import com.jrj.smartHome.LogoSplashActivity;
//import com.jrj.smartHome.util.AppManager;

/**
 * 离线推送消息管理类
 */
public class ProcessPushMsgProxy {
    public static Context mContext;

    /**
     * 自定义的消息格式
     *
     * @param context 上下文对象
     * @param msg     自定义推送信息
     */
    public static void processPushMsg(Context context, String msg) {
        Logger.e("ProcessPushMsgProxy.clazz----------->>>message define msg:" + msg);
        String oldMsg = SPUtils.getInstance().getString("msg", "");
        if (oldMsg.equals(msg)) {
            return;
        }

        SPUtils.getInstance().put("msg", msg);
        NotificationManager mNotificationManager = (NotificationManager) context.
                getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder1 = new Notification.Builder(context);
        builder1.setSmallIcon(R.mipmap.ic_launcher); //设置图标
        builder1.setContentTitle("金融街悦生活"); //设置标题
        builder1.setContentText(msg); //消息内容
        builder1.setWhen(System.currentTimeMillis()); //发送时间
        builder1.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
        builder1.setAutoCancel(true);//打开程序后图标消失
        Intent intent1 = new Intent(Intent.ACTION_MAIN);
        intent1.setClass(context, context.getClass());
        //当任务栏上该通知被点击时执行的页面跳转
        PendingIntent pintent = PendingIntent.getActivity(context, 0, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder1.setContentIntent(pintent);
        Notification notify = builder1.build();
        mNotificationManager.notify((int) System.currentTimeMillis(), notify);
    }

    /**
     * 通知消息处理
     *
     * @param context       上下文
     * @param notifyTitle   标题
     * @param notifyContent 内容
     */

    public static void processPushMsg(Context context, String notifyTitle, String notifyContent) {
        Logger.e("ProcessPushMsgProxy.class->notifyTitle:" + notifyTitle + ",notifyContent:" + notifyContent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder1 = new Notification.Builder(context);
        builder1.setSmallIcon(R.mipmap.ic_launcher); //设置图标
        //builder1.setTicker("显示第二个通知");
        mContext = context;
        if (notifyContent.indexOf("changePhoneNumber") > 0) {
            builder1.setContentTitle("更换手机号"); //设置标题
            Intent inviteIntent = new Intent(ACTION_JGPUSH_CHANGEPHONENUMBER);
            AppConfig.JGPushMsg = notifyTitle;
            AppConfig.JGPushContent = "更换手机号";
            mContext.sendBroadcast(inviteIntent);
        } else if (notifyContent.indexOf("smartHome") > 0) {
            builder1.setContentTitle("智能家居");
            Intent inviteIntent = new Intent(ACTION_JGPUSH_SMARTHOME);
            AppConfig.JGPushMsg = notifyTitle;
            AppConfig.JGPushContent = "智能家居";
            mContext.sendBroadcast(inviteIntent);
        } else if (notifyContent.indexOf("propertyAnnouncement") > 0) {
            builder1.setContentTitle("物业公告");
            Intent inviteIntent = new Intent(ACTION_JGPUSH_PROPERTYANNOUNCEMENT);
            AppConfig.JGPushMsg = notifyTitle;
            AppConfig.JGPushContent = "物业公告";
            mContext.sendBroadcast(inviteIntent);
        } else if (notifyContent.indexOf("activityRecommendation") > 0) {
            builder1.setContentTitle("活动推荐");
            Intent inviteIntent = new Intent(ACTION_JGPUSH_ACTIVITYRECOMMENDATION);
            AppConfig.JGPushMsg = notifyTitle;
            AppConfig.JGPushContent = "活动推荐";
            mContext.sendBroadcast(inviteIntent);
        } else if (notifyContent.indexOf("hawkeyeMonitoring") > 0) {
            builder1.setContentTitle("鹰眼监控");
            Intent inviteIntent = new Intent(ACTION_JGPUSH_HAWKEYEMONITORING);
            AppConfig.JGPushMsg = notifyTitle;
            AppConfig.JGPushContent = "鹰眼监控";
            mContext.sendBroadcast(inviteIntent);
        } else if (notifyContent.indexOf("smartVisitor") > 0) {
            builder1.setContentTitle("智慧访客");
            Intent inviteIntent = new Intent(ACTION_JGPUSH_SMARTVISITOR);
            AppConfig.JGPushMsg = notifyTitle;
            AppConfig.JGPushContent = "智慧访客";
            mContext.sendBroadcast(inviteIntent);
        } else if (notifyContent.indexOf("familyCare") > 0) {
            builder1.setContentTitle("亲人关怀");
            Intent inviteIntent = new Intent(ACTION_JGPUSH_FAMILYCARE);
            AppConfig.JGPushMsg = notifyTitle;
            AppConfig.JGPushContent = "亲人关怀";
            mContext.sendBroadcast(inviteIntent);
        } else if (notifyContent.indexOf("addFamilyMembers") > 0) {
            builder1.setContentTitle("家庭成员邀请");
            Intent inviteIntent = new Intent(ACTION_JGPUSH_ADDFAMILYMEMBERS);
            AppConfig.JGPushMsg = notifyTitle;
            AppConfig.JGPushContent = "家庭成员邀请";
            mContext.sendBroadcast(inviteIntent);
        } else if (notifyContent.indexOf("deleteFamilyMember") > 0) {
            builder1.setContentTitle("删除家庭成员");
            Intent inviteIntent = new Intent(ACTION_JGPUSH_DELETEFAMILYMEMBER);
            AppConfig.JGPushMsg = notifyTitle;
            AppConfig.JGPushContent = "删除家庭成员";
            mContext.sendBroadcast(inviteIntent);
        } else if (notifyContent.indexOf("bindHouse") > 0) {
            builder1.setContentTitle("房屋绑定审核通过");
            Intent inviteIntent = new Intent(ACTION_JGPUSH_BINDINGHOUSE);
            AppConfig.JGPushMsg = notifyTitle;
            AppConfig.JGPushContent = "房屋绑定审核通过";
            mContext.sendBroadcast(inviteIntent);
        } else if (notifyContent.indexOf("remoteLogin") > 0) {
            builder1.setContentTitle("异地登录提醒");
            Intent inviteIntent = new Intent(ACTION_JGPUSH_REMOTELOGIN);
            AppConfig.JGPushMsg = notifyTitle;
            AppConfig.JGPushContent = "您的账号已在其他设备登录";
            mContext.sendBroadcast(inviteIntent);
        } else {
            return;
        }
        builder1.setContentText(notifyTitle); //消息内容
        builder1.setWhen(System.currentTimeMillis()); //发送时间
        builder1.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
        builder1.setAutoCancel(true);//打开程序后图标消失
        Intent intent1 = new Intent(Intent.ACTION_MAIN);
        intent1.setClass(context, context.getClass());
        //当任务栏上该通知被点击时执行的页面跳转
        PendingIntent pintent = PendingIntent.getActivity(context, 0, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder1.setContentIntent(pintent);
        mNotificationManager.notify(0, builder1.build());
    }
}
