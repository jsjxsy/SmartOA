package com.gx.smart.lib.http.api;

import com.gx.smart.lib.http.base.CallBack;
import com.gx.smart.lib.http.base.GrpcAsyncTask;
import com.gx.wisestone.work.app.grpc.push.AppMessagePushProviderGrpc;
import com.gx.wisestone.work.app.grpc.push.UpdateMessagePushDto;
import com.gx.wisestone.work.app.grpc.push.UpdateMessagePushResponse;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;


public class AppMessagePushService {

    //25秒，网络请求超时
    public static final int TIMEOUT_NETWORK = 25;

    private static AppMessagePushService UserCenterClient;

    private AppMessagePushService() {

    }

    public static AppMessagePushService getInstance() {
        if (UserCenterClient == null) {
            UserCenterClient = new AppMessagePushService();
        }
        return UserCenterClient;
    }

    /**
     * 获取UserCenterstub  设置超时时间  8秒
     *
     * @param channel
     * @return
     */
    public AppMessagePushProviderGrpc.AppMessagePushProviderBlockingStub getMessagePushStub(ManagedChannel channel) {
        return AppMessagePushProviderGrpc.newBlockingStub(channel)
                .withDeadlineAfter(TIMEOUT_NETWORK, TimeUnit.SECONDS);

    }


    /**
     * 上传消息推送设备码
     *
     * @param jg_app_id 极光返回的设备Id
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, UpdateMessagePushResponse> updateMessagePush(final String jg_app_id, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, UpdateMessagePushResponse>(callBack) {
            @Override
            protected UpdateMessagePushResponse doRequestData(ManagedChannel channel) {
                UpdateMessagePushDto message = UpdateMessagePushDto.newBuilder()
                        .setJgAppId(jg_app_id)
                        .build();
                Logger.d(message);
                UpdateMessagePushResponse response = null;
                try {
                    response = getMessagePushStub(channel).updateMessagePush(message);
                    Logger.d(response);
                } catch (Exception e) {
                    Logger.e(e, "updateMessagePush");
                }

                return response;
            }
        }.doExecute();
    }


}
