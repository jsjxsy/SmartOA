package com.gx.smart.smartoa.data.network;

/**
 * @author xiaosy
 * @create 2019-12-04
 * @Describe
 **/
public class ApiConfig {
    //| test.huishi.cloud | 32331/gRPC | 办公APP服务 | svc-ws-work-app-service.ws-work |
    public static final String USER_SERVER_URL = "test.huishi.cloud";
    //common
    public static final String USER_SERVER_PORT = "32331";
    //auth
    public static final String AUTH_API_SERVER_PORT = "31772";
    //紫光
    public static final String ZG_SERVICE_PORT = "30501";
    //天气
    public static String GENERAL_INFO_SERVICE_PORT = "30155";
    //upload image
    public static final String UPLOAD_IMAGE_SERVER_PORT="30691";
    //space
    public static final String USER_SPACE = "b60bcfe2";//用户池
    //web socket
    public static final String JoyLife_gRPC_Server_2_FORMAL = "api.huishi.cloud";
    public static final String WEB_SOCKET_URL = "wss://" + JoyLife_gRPC_Server_2_FORMAL + ":30152/websocket/{ID}/{TOKEN}/app";//WebSocket通知地址
    public static final String WEB_SOCKET_TEST_URL = "ws://192.168.137.240:30152/websocket/{ID}/{TOKEN}/app";//WebSocket通知地址
    //协议
    public static final String WEB_AGREEMENT_URL = "http://joylife.huishi.cloud/work/useage.html";
    //company
    public static final String COMPANY_ACTION_URL = "http://work.huishi.cloud/work/htgx.html";
}
