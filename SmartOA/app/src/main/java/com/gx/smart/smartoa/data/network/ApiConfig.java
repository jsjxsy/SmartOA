package com.gx.smart.smartoa.data.network;

/**
 * @author xiaosy
 * @create 2019-12-04
 * @Describe
 **/
public class ApiConfig {
    public static final String USER_SERVER_URL = "192.168.139.201";
    //
    public static final String USER_SERVER_PORT = "32331";
    //auth
    public static final String AUTH_API_SERVER_PORT = "31772";
    //紫光
    public static final String ZG_SERVICE_PORT = "30501";
    //
    public static final String ATTANDANCE_SERVER_PORT = "31003";
    //天气
    public static String GENERALINFO_SERVICE_PORT = "30155";


    public static final String HUISHI_USER_SPACE = "b60bcfe2";//用户池

    public static final String AGREEMENT_FORMAL_URL = "http://joylife.jrjkg.com.cn/AppH5/useage.html";//协议URL

    public static final String JoyLife_gRPC_Server_2_FORMAL = "api.huishi.cloud";
    public static final String WEB_SOCKET_URL = "wss://" + JoyLife_gRPC_Server_2_FORMAL + ":30152/websocket/{ID}/{TOKEN}/app";//WebSocket通知地址

    public static final String WEB_SOCKET_TEST_URL = "ws://192.168.137.240:30152/websocket/{ID}/{TOKEN}/app";//WebSocket通知地址
}
