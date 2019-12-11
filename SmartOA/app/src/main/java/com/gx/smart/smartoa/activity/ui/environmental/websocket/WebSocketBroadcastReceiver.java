package com.gx.smart.smartoa.activity.ui.environmental.websocket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;


public class WebSocketBroadcastReceiver  extends BroadcastReceiver {

    private Handler handler = null;

    public  WebSocketBroadcastReceiver(){
        handler = null;
        Log.i("joylife","WebSocketBroadcastReceiver created:");
    }
    public  WebSocketBroadcastReceiver(Handler handler){
        this.handler = handler;
        Log.i("joylife","WebSocketBroadcastReceiver created222:");
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        Log.i("joylife","Message peekService:"+service);
        return super.peekService(myContext, service);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        WsNotifyBean notifyBean = (WsNotifyBean) intent.getSerializableExtra("notifyBean");
        if(null != notifyBean){
            Log.i("joylife","Message details:"+notifyBean);
            Log.i("joylife","Message details:"+notifyBean.getUuid());

            if(null != handler){
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putParcelable("notifyBean",notifyBean);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }
    }
}
