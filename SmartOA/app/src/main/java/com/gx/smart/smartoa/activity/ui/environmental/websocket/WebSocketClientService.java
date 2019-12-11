package com.gx.smart.smartoa.activity.ui.environmental.websocket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;


public class WebSocketClientService extends Service {

    WebSocketNotifyClient wsClient = null;


    @Override
    public void onCreate() {
        super.onCreate();

        if(null == wsClient){
            Log.i("joylife","WebSocketClientService- Thread ID = " + Thread.currentThread().getId());
            wsClient = WebSocketNotifyClient.getInstance();
            wsClient.setContext(this);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("joylife", "WebSocketClientService onBind - Thread ID = " + Thread.currentThread().getId());
        String id = intent.getStringExtra("id");
        String token = intent.getStringExtra("token");
        wsClient.connect(id,token);
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("joylife", "WebSocketClientService unBind - Thread ID = " + Thread.currentThread().getId());
        if(null != wsClient){
            wsClient.close();
        }
        return super.onUnbind(intent);
    }
}
