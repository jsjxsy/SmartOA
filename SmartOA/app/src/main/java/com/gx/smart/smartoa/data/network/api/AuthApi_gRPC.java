//package com.gx.smart.smartoa.data.network.api;
//
//
//import android.util.Log;
//
//
//import java.util.concurrent.TimeUnit;
//
//import io.grpc.ManagedChannel;
//
//import static com.jrj.smartHome.AppConfig.mIsFormal;
//
//public class AuthApi_gRPC {
//
//    private static String user_space = mIsFormal ? AppConfig.HUISHI_USER_SPACE_FORMAL :
//            AppConfig.HUISHI_USER_SPACE_TEXT;
//    public interface myCallBack {
//        public void fail(int error, String msg);
//    }
//
//    private static AuthApi_gRPC AuthClient;
//
//    private AuthApi_gRPC() {
//
//    }
//
//    public static AuthApi_gRPC getInstance() {
//        if (AuthClient == null) {
//            AuthClient = new AuthApi_gRPC();
//        }
//        return AuthClient;
//    }
//
//    /**
//     * 获取AuthStub  设置超时时间  8秒
//     *
//     * @param channel
//     * @return
//     */
//    public static AuthApiGrpc.AuthApiBlockingStub getAuthStub(ManagedChannel channel) {
//        return AuthApiGrpc.newBlockingStub(channel)
//                .withDeadlineAfter(UserCenter_gRpc.TIMEOUT_NETWORK, TimeUnit.SECONDS);
//
//    }
//
//    /**
//     * 用户注册
//     *
//     * @param user_param 用户信息
//     * @param account (必) 账号/手机号 根据登录类型而定
//     * @param password (必) 密码/验证码 根据登录类型而定
//     * @param mobile 登录手机号
//     * @param mobile_verify_code 手机验证码
//     * @return callBack返回值
//     */
//    public static AuthGrpcAsyncTask<String, Void, RegistResp> regist(final String account, final String password, final String mobile, final String mobile_verify_code, CallBack callBack) {
//        return new AuthGrpcAsyncTask<String, Void, RegistResp>(callBack) {
//            @Override
//            protected RegistResp doRequestData(ManagedChannel channel) {
//                RegistReq message = RegistReq.newBuilder()
//                        .setUserSpace(user_space)
//                        .setAccount(account)
//                        .setPassword(password)
//                        .setMobile(mobile)
//                        .setMobileVerifyCode(mobile_verify_code)
//                        .build();
//                RegistResp response = null;
//                try {
//                    response = getAuthStub(channel).regist(message);
//                }catch (Exception e) {
//                    Log.i("AuthApi_gRPC", e.getMessage());
//                }
//
//                return response;
//            }
//        }.doExecute();
//    }
//
//
//
//    /**
//     * 用户信息修改
//     *
//     * @param account (必) 账号/手机号 根据登录类型而定
//     * @param password (必) 密码/验证码 根据登录类型而定
//     * @param mobile 登录手机号
//     * @param mobile_verify_code 手机验证码
//     * @return callBack返回值
//     */
//    public static AuthGrpcAsyncTask<String, Void, UserModifyResp> userModify(final String account, final String password, final String mobile, final String mobile_verify_code, final String userId, final String token, CallBack callBack) {
//        return new AuthGrpcAsyncTask<String, Void, UserModifyResp>(callBack) {
//            @Override
//            protected UserModifyResp doRequestData(ManagedChannel channel) {
//                UserModifyReq message = UserModifyReq.newBuilder()
//                        .setUserSpace(user_space)
//                        .setAccount(account)
//                        .setPassword(password)
//                        .setMobile(mobile)
//                        .setMobileVerifyCode(mobile_verify_code)
//                        .setUserId(userId)
//                        .setToken(token)
//                        .build();
//                UserModifyResp response = null;
//                try {
//                    response = getAuthStub(channel).userModify(message);
//                } catch (Exception e) {
//                    Log.i("AuthApi_gRPC", e.getMessage());
//                }
//
//                return response;
//            }
//        }.doExecute();
//    }
//
//    /**
//     * 用户手机号修改
//     *
//     * @param account            (必) 账号/手机号 根据登录类型而定
//     * @param password           (必) 密码/验证码 根据登录类型而定
//     * @param mobile             登录手机号
//     * @param mobile_verify_code 手机验证码
//     * @return callBack返回值
//     */
//    public static AuthGrpcAsyncTask<String, Void, UserModifyResp> userModifyMobile(final String mobile, final String mobile_verify_code, final String userId, final String token, CallBack callBack) {
//        return new AuthGrpcAsyncTask<String, Void, UserModifyResp>(callBack) {
//            @Override
//            protected UserModifyResp doRequestData(ManagedChannel channel) {
//                UserModifyReq message = UserModifyReq.newBuilder()
//                        .setUserSpace(user_space)
//                        .setMobile(mobile)
//                        .setMobileVerifyCode(mobile_verify_code)
//                        .setUserId(userId)
//                        .setToken(token)
//                        .build();
//                UserModifyResp response = null;
//                try {
//                    response = getAuthStub(channel).userModify(message);
//                } catch (Exception e) {
//                    Log.i("AuthApi_gRPC", e.getMessage());
//                }
//
//                return response;
//            }
//        }.doExecute();
//    }
//
//    /**
//     * 用户密码修改
//     *
//     * @param password (必) 密码/验证码 根据登录类型而定
//     * @return callBack返回值
//     */
//    public static AuthGrpcAsyncTask<String, Void, UserModifyResp> userModifyPassWord(final String password, final String userId, final String token, CallBack callBack) {
//        return new AuthGrpcAsyncTask<String, Void, UserModifyResp>(callBack) {
//            @Override
//            protected UserModifyResp doRequestData(ManagedChannel channel) {
//                UserModifyReq message = UserModifyReq.newBuilder()
//                        .setUserSpace(user_space)
//                        .setPassword(password)
//                        .setUserId(userId)
//                        .setToken(token)
//                        .build();
//                UserModifyResp response = null;
//                try {
//                    response = getAuthStub(channel).userModify(message);
//                }catch (Exception e) {
//                    Log.i("AuthApi_gRPC", e.getMessage());
//                }
//
//                return response;
//            }
//        }.doExecute();
//    }
//
//    /**
//     * 登陆
//     *
//     * @param account (必) 账号/手机号 根据登录类型而定
//     * @param password (必) 密码/验证码 根据登录类型而定
//     * @param login_type 登录类型 1 账号; 2 手机号; 3 短信验证码
//     * @return callBack返回值
//     */
//    public static AuthGrpcAsyncTask<String, Void, LoginResp> login(final String account, final String password,final int login_type, CallBack callBack) {
//        return new AuthGrpcAsyncTask<String, Void, LoginResp>(callBack) {
//            @Override
//            protected LoginResp doRequestData(ManagedChannel channel) {
//                LoginReq message = LoginReq.newBuilder()
//                        .setUserSpace(user_space)
//                        .setAccount(account)
//                        .setPassword(password)
//                        .setLoginType(login_type)
//                        .build();
//                LoginResp response = null;
//                try {
//                    response = getAuthStub(channel).login(message);
//                }catch (Exception e) {
//                    Log.i("AuthApi_gRPC", e.getMessage());
//                }
//
//                return response;
//            }
//        }.doExecute();
//    }
//
//
//    /**
//     * 刷新token
//     *
//     * @param token 秘钥
//     * @param refresh_token 刷新秘钥
//     * @param account 用户名
//     * @return callBack返回值
//     */
//    public static AuthGrpcAsyncTask<String, Void, RefreshTokenResp> refreshToken(final String token,final String refresh_token, final String account, CallBack callBack) {
//        return new AuthGrpcAsyncTask<String, Void, RefreshTokenResp>(callBack) {
//            @Override
//            protected RefreshTokenResp doRequestData(ManagedChannel channel) {
//                RefreshTokenReq message = RefreshTokenReq.newBuilder()
//                        .setUserSpace(user_space)
//                        .setToken(token)
//                        .setRefreshToken(refresh_token)
//                        .setAccount(account)
//                        .build();
//                RefreshTokenResp response = null;
//                try {
//                    response = getAuthStub(channel).refreshToken(message);
//                }catch (Exception e) {
//                    Log.i("AuthApi_gRPC", e.getMessage());
//                }
//
//                return response;
//            }
//        }.doExecute();
//    }
//
//
//    /**
//     * tokenKeys
//     *
//     * @param type 类型
//     * @param user_space 手机号
//     * @return callBack返回值
//     */
//    public static AuthGrpcAsyncTask<String, Void, TokenKeysResp> tokenKeys(final int type, final String user_space, CallBack callBack) {
//        return new AuthGrpcAsyncTask<String, Void, TokenKeysResp>(callBack) {
//            @Override
//            protected TokenKeysResp doRequestData(ManagedChannel channel) {
//                TokenKeysReq message = TokenKeysReq.newBuilder()
//                        .setType(type)
//                        .setUserSpace(user_space)
//                        .build();
//                TokenKeysResp response = null;
//                try {
//                    response = getAuthStub(channel).tokenKeys(message);
//                }catch (Exception e) {
//                    Log.i("AuthApi_gRPC", e.getMessage());
//                }
//
//                return response;
//            }
//        }.doExecute();
//    }
//
//
//    /**
//     * 验证码
//     *
//     * @param target 验证码 目标
//     * @param targetType 目标类型 1 手机号; 2 邮箱; 3 微信;
//     * @param purpose 用途 1 登录 2 注册 3 修改
//     * @return callBack返回值
//     */
//    public static AuthGrpcAsyncTask<String, Void, VerifyCodeResp> verifyCode(final String target, final int targetType, final int purpose, CallBack callBack) {
//        return new AuthGrpcAsyncTask<String, Void, VerifyCodeResp>(callBack) {
//            @Override
//            protected VerifyCodeResp doRequestData(ManagedChannel channel) {
//                VerifyCodeReq message = VerifyCodeReq.newBuilder()
//                        .setUserSpace(user_space)
//                        .setTarget(target)
//                        .setTargetType(targetType)
//                        .setPurpose(purpose)
//                        .build();
//                VerifyCodeResp response = null;
//                try {
//                    response = getAuthStub(channel).verifyCode(message);
//                }catch (Exception e) {
//                    Log.i("AuthApi_gRPC", e.getMessage());
//                }
//
//                return response;
//            }
//        }.doExecute();
//    }
//}