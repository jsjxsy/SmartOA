package com.gx.smart.smartoa.data.network.api;

import android.util.Log;

import com.google.protobuf.ByteString;
import com.gx.smart.smartoa.data.network.api.base.CallBack;
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask;
import com.gx.wisestone.work.app.grpc.appuser.AddOpinionRequest;
import com.gx.wisestone.work.app.grpc.appuser.AppInfoResponse;
import com.gx.wisestone.work.app.grpc.appuser.AppLogoutRequest;
import com.gx.wisestone.work.app.grpc.appuser.AppUserCenterInterfaceGrpc;
import com.gx.wisestone.work.app.grpc.appuser.BindAppUserRequest;
import com.gx.wisestone.work.app.grpc.appuser.GetAppUserInfoRequest;
import com.gx.wisestone.work.app.grpc.appuser.UpdateAppUserRequest;
import com.gx.wisestone.work.app.grpc.appuser.VerifiedRequest;
import com.gx.wisestone.work.app.grpc.common.CommonResponse;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;


public class UserCenterService {

    //25秒，网络请求超时
    public final int TIMEOUT_NETWORK = 25;

    private static UserCenterService UserCenterClient;

    private UserCenterService() {

    }

    public static UserCenterService getInstance() {
        if (UserCenterClient == null) {
            UserCenterClient = new UserCenterService();
        }
        return UserCenterClient;
    }

    /**
     * 获取UserCenterstub  设置超时时间  8秒
     *
     * @param channel
     * @return
     */
    public AppUserCenterInterfaceGrpc.AppUserCenterInterfaceBlockingStub getUserStub(ManagedChannel channel) {
        return AppUserCenterInterfaceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(TIMEOUT_NETWORK, TimeUnit.SECONDS);

    }


    /**
     * 绑定app用户
     * <p>
     * string nick_name = 2;  //昵称
     * string name = 3;  //用户姓名
     * int32 gender = 4;  //性别1.男2女
     * string mobile = 5;  //联系方式
     * int32 age = 6;  //年龄
     * int32 identity_type = 7;  //证件类型1居民身份证2居住证3签证4护照5户口本
     * string identity_card = 8;  //证件号
     * int32 msg_notice = 9;  //消息通知 1开启 2关闭
     * int32 type = 12;  //用户类别1户主
     * int32 alarm_notice = 13;  //报警通知 1开启 2关闭
     *
     * @param mobile 手机号
     * @param name   姓名
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AppInfoResponse> bindAppUser(final String mobile, final String name, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AppInfoResponse>(callBack) {
            @Override
            protected AppInfoResponse doRequestData(ManagedChannel channel) {
                BindAppUserRequest message = BindAppUserRequest.newBuilder()
                        .setMobile(mobile)
                        .setName(name)
                        .build();
                AppInfoResponse response = null;
                try {
                    response = getUserStub(channel).bindAppUser(message);
                } catch (Exception e) {
                    Log.i("UserCenterService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }

    /**
     * 更新app信息
     *
     * @param name 姓名
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AppInfoResponse> updateAppUserName(final String name, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AppInfoResponse>(callBack) {
            @Override
            protected AppInfoResponse doRequestData(ManagedChannel channel) {
                UpdateAppUserRequest message = UpdateAppUserRequest.newBuilder()
                        .setName(name)
                        .build();
                AppInfoResponse response = null;
                try {
                    response = getUserStub(channel).updateAppUser(message);
                } catch (Exception e) {
                    Log.i("UserCenterService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }

    /**
     * 更新app信息
     *
     * @param nickName 姓名
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AppInfoResponse> updateAppUserNickName(final String nickName, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AppInfoResponse>(callBack) {
            @Override
            protected AppInfoResponse doRequestData(ManagedChannel channel) {
                UpdateAppUserRequest message = UpdateAppUserRequest.newBuilder()
                        .setNickName(nickName)
                        .build();
                AppInfoResponse response = null;
                try {
                    response = getUserStub(channel).updateAppUser(message);
                } catch (Exception e) {
                    Log.i("UserCenterService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }

    /**
     * 更新app信息
     *
     * @param mobile 手机号
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AppInfoResponse> updateAppUserMobile(final String mobile, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AppInfoResponse>(callBack) {
            @Override
            protected AppInfoResponse doRequestData(ManagedChannel channel) {
                UpdateAppUserRequest message = UpdateAppUserRequest.newBuilder()
                        .setMobile(mobile)
                        .build();
                AppInfoResponse response = null;
                try {
                    response = getUserStub(channel).updateAppUser(message);
                } catch (Exception e) {
                    Log.i("UserCenterService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }

    /**
     * 意见反馈
     *
     * @param content 内容
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, CommonResponse> addOpinion(final String content, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                AddOpinionRequest message = AddOpinionRequest.newBuilder()
                        .setContent(content)
                        .build();
                CommonResponse response = null;
                try {
                    response = getUserStub(channel).addOpinion(message);
                } catch (Exception e) {
                    Log.i("UserCenterService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }

    /**
     * 更新消息推送设置
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, CommonResponse> verified(final String name, final String identityCard, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                VerifiedRequest message = VerifiedRequest.newBuilder()
                        .setIdentityCard(identityCard)
                        .setName(name)
                        .build();
                CommonResponse response = null;
                try {
                    response = getUserStub(channel).verified(message);
                } catch (Exception e) {
                    Log.i("UserCenterService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }


    /**
     * 更新消息推送设置
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AppInfoResponse> changeUserGender(final int gender, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AppInfoResponse>(callBack) {
            @Override
            protected AppInfoResponse doRequestData(ManagedChannel channel) {
                UpdateAppUserRequest message = UpdateAppUserRequest.newBuilder()
                        .setGender(gender)
                        .build();
                AppInfoResponse response = null;
                try {
                    response = getUserStub(channel).updateAppUser(message);
                } catch (Exception e) {
                    Log.i("UserCenterService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }

    /**
     * 修改用户信息
     *
     * @param data 图片
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AppInfoResponse> updateAppUserImage(final ByteString data, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AppInfoResponse>(callBack) {
            @Override
            protected AppInfoResponse doRequestData(ManagedChannel channel) {
                UpdateAppUserRequest message = UpdateAppUserRequest.newBuilder()
                        .setImageBytes(data)
                        .build();
                AppInfoResponse response = null;
                try {
                    response = getUserStub(channel).updateAppUser(message);
                } catch (Exception e) {
                    Log.i("UserCenterService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }

    /**
     * 获取用户信息
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AppInfoResponse> getAppUserInfo(CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AppInfoResponse>(callBack) {
            @Override
            protected AppInfoResponse doRequestData(ManagedChannel channel) {
                GetAppUserInfoRequest message = GetAppUserInfoRequest.newBuilder()
                        .build();
                AppInfoResponse response = null;
                try {
                    response = getUserStub(channel).getAppUserInfo(message);
                } catch (Exception e) {
                    Log.i("UserCenterService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }


    /**
     * 退出登录
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, CommonResponse> appLogout(CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                AppLogoutRequest message = AppLogoutRequest.newBuilder()
                        .build();
                CommonResponse response = null;
                try {
                    response = getUserStub(channel).appLogout(message);
                } catch (Exception e) {
                    Log.i("UserCenterService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }


}
