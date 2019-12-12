package com.gx.smart.smartoa.activity.ui.environmental.websocket;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.gx.smart.smartoa.data.network.ApiConfig;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by edgar.liu on 2019/6/13.
 */
public class WebSocketNotifyClient {
    static WebSocketNotifyClient gInstance = null;

    private Context context = null;
    private WebSocketClient wsClient = null;
    private Thread clientThread = null;
    private String id = null;
    private String token = null;
    private String preId = "";
    private String preToken = "";

    private Handler handler = null;

    public WebSocketNotifyClient() {
        gInstance = this;
    }

    static public WebSocketNotifyClient getInstance() {
        if (null == gInstance) {
            gInstance = new WebSocketNotifyClient();
        }
        return gInstance;
    }

    public int connect(String id, String token) {
        setId(id);
        setToken(token);
        return connect();
    }

    public int connect() {
        if ((null == id) || (null == token)) {
            return -1;
        }

        if (null != wsClient) {
            if ((preId.compareToIgnoreCase(id) == 0) && (preToken.compareToIgnoreCase(token) == 0)) {
                return 0;
            }
            wsClient.close();
            if (null != clientThread) {
//                clientThread.stop();
            }
            wsClient = null;
            clientThread = null;
            preId = id;
            preToken = token;
        }
        Draft draft = new Draft_6455();
        String wslocation = "";
        if (true) {
            wslocation = ApiConfig.WEB_SOCKET_URL;
        } else {
            wslocation = ApiConfig.WEB_SOCKET_TEST_URL;
        }
        String str = wslocation;
        str = str.replaceAll("\\{ID\\}", id);
        str = str.replaceAll("\\{TOKEN\\}", token);
        System.out.println("uri: " + str);
        URI uri = null;
        try {
            uri = new URI(str);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return -1;
        }

        if (null == wsClient) {
            Log.i("joylife", "WebSocketClient connect: " + uri);
            wsClient = new WebSocketClient(uri, draft) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    if ((null != serverHandshake.getContent()) && (serverHandshake.getContent().length > 0)) {
                        Log.i("joylife", new String(serverHandshake.getContent()));
                    }
                }

                @Override
                public void onMessage(String s) {
                    parseMsg(s);
                }

                @Override
                public void onClose(int code, String reason, boolean b) {
                    Log.i("joylife", "Closed: " + code + " " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    Log.i("joylife", "Error: ");
                    ex.printStackTrace();
                }
            };
            try {
                clientThread = new Thread(wsClient);
                clientThread.start();
                //clientThread.join();
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                //wsClient.close();
            }
        }

        return 0;

    }

    public void close() {
        if (null != wsClient) {
            wsClient.close();
            clientThread = null;
            wsClient = null;
        }
    }


    public void parseMsg(String json) {
        if ((null == json) || (json.isEmpty())) {
            //TODO 仅是测试消息
            json = "{\"service\":\"network_report\",\"uuid\":12,\"devName\":\"设备名称\",\"category\":\"xxxx\",\"model\":\"xxxx\",\"val\":1,\"channel\":1}";
        }
        WsNotifyBean notifyBean = JSON.parseObject(json, WsNotifyBean.class);
        Log.i("joylife", "notifyBean:" + notifyBean.getUuid());
        //使用handler方式发送到UI线程
        if (null != handler) {
            if (null == notifyBean.getUuid()) {
                notifyBean.setUuid("");
            }
            if (null == notifyBean.getCategory()) {
                notifyBean.setCategory("");
            }
            if (null == notifyBean.getModel()) {
                notifyBean.setModel("");
            }
            if (null == notifyBean.getDevName()) {
                notifyBean.setDevName("");
            }
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("notifyBean", notifyBean);
            msg.setData(bundle);
            handler.sendMessage(msg);
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

}
