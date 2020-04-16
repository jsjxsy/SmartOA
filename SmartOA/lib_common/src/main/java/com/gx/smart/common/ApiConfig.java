package com.gx.smart.common;

/**
 * @author xiaosy
 * @create 2019-12-04
 * @Describe 网络配置
 **/
public class ApiConfig {
    //| test.huishi.cloud | 32331/gRPC | 办公APP服务 | svc-ws-work-app-service.ws-work |
    //| work.huishi.cloud | 30007/gRPC | 办公数据库服务 | svc-ws-work-datasource.ws-work |
    public static final String USER_SERVER_URL = "work.huishi.cloud";
    //common
    public static final String USER_SERVER_PORT = "32331";
    //auth
    public static final String AUTH_API_SERVER_URL = "uaa.huishi.cloud";
    public static final String AUTH_API_SERVER_PORT = "31772";
    //紫光
    public static final String ZG_SERVICE_URL = "api.huishi.cloud";
    public static final String ZG_SERVICE_PORT = "30501";
    //upload image
    public static final String UPLOAD_IMAGE_SERVER_URL = "api.huishi.cloud";
    public static final String UPLOAD_IMAGE_SERVER_PORT = "30691";
    //user space
    public static final String USER_SPACE = "b60bcfe2";//用户池
    //web socket
    //WebSocket通知地址
    public static final String WEB_SOCKET_URL = "wss://" + ZG_SERVICE_URL + ":30152/websocket/{ID}/{TOKEN}/app";
    //协议
    public static final String WEB_AGREEMENT_URL = "http://joylife.huishi.cloud/work/useage.html";
    //company
    public static final String COMPANY_ACTION_URL = "http://work.huishi.cloud/work/htgx.html";
    //天气
    public static String GENERAL_INFO_SERVICE_PORT = "30155";
    //
    public static final String SERVER_ERROR_MESSAGE="服务器错误";

}
