package com.gx.smart.push;

import android.content.Context;

/**
 * @author xiaosy
 * @create 2019-12-18
 * @Describe
 **/
public interface PushProxyCallBack {
    void processPushMsg(Context context, String msg);

    void processPushMsg(Context context, String notifyTitle, String notifyContent);
}
