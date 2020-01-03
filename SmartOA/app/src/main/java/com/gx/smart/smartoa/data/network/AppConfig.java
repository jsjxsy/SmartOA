package com.gx.smart.smartoa.data.network;

/**
 * @author xiaosy
 * @create 2019-12-05
 * @Describe
 **/
public class AppConfig {
    public static int currentSysTenantNo = 2;
    public static String userId;
    public static String loginToken;
    public static String refreshToken;
    public static String mJiGuangToken;
    public static long employeeId;


    //是不是第一次登录
    public static final String SH_FIRST_OPEN = "FIRST_OPEN";
    //保存登录用户信息
    public static final String SH_USER_ACCOUNT = "account";
    public static final String SH_USER_REAL_NAME = "userName";
    public static final String SH_PASSWORD = "password";

    public static final String PLACE_NAME = "place";
    public static final String COMPANY_APPLY_STATUS = "status";

    public static String JGPushMsg;
    public static String JGPushContent;

    public static String SMART_HOME_SN = "1741hf14f2df425db6f228e21b3d8d86";
    public static long ROOM_ID = 19;

    public static final String REPAIR_PREFIX = "repair";
}
