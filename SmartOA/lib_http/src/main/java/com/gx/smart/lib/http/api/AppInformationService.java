package com.gx.smart.lib.http.api;

import com.gx.smart.lib.http.base.CallBack;
import com.gx.smart.lib.http.base.GrpcAsyncTask;
import com.gx.wisestone.core.grpc.lib.common.QueryDto;
import com.gx.wisestone.work.app.grpc.information.AnnouncementRequest;
import com.gx.wisestone.work.app.grpc.information.AppAnnouncementResponse;
import com.gx.wisestone.work.app.grpc.information.AppInformationGrpc;
import com.gx.wisestone.work.app.grpc.information.AppInformationResponse;
import com.gx.wisestone.work.app.grpc.information.MessageReadRequest;
import com.gx.wisestone.work.app.grpc.information.MessageReadResponse;
import com.gx.wisestone.work.app.grpc.information.PersonInformationRequest;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;


public class AppInformationService {

    //25秒，网络请求超时
    public static final int TIMEOUT_NETWORK = 25;

    private static AppInformationService UserCenterClient;

    private AppInformationService() {

    }

    public static AppInformationService getInstance() {
        if (UserCenterClient == null) {
            UserCenterClient = new AppInformationService();
        }
        return UserCenterClient;
    }

    /**
     * 获取UserCenterstub  设置超时时间  8秒
     *
     * @param channel
     * @return
     */
    public static AppInformationGrpc.AppInformationBlockingStub getUserStub(ManagedChannel channel) {
        return AppInformationGrpc.newBlockingStub(channel)
                .withDeadlineAfter(TIMEOUT_NETWORK, TimeUnit.SECONDS);

    }


    /**
     * 个人信息
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AppInformationResponse> getInformation(final QueryDto query, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AppInformationResponse>(callBack) {
            @Override
            protected AppInformationResponse doRequestData(ManagedChannel channel) {
                PersonInformationRequest message = PersonInformationRequest.newBuilder()
                        .setQuery(query)
                        .build();
                Logger.d(message);
                AppInformationResponse response = null;
                try {
                    response = getUserStub(channel).getInformation(message);
                    Logger.d(response);
                } catch (Exception e) {
                    Logger.e(e, "getInformation");
                }

                return response;
            }
        }.doExecute();
    }

    /**
     * 物业公告
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AppAnnouncementResponse> getAnnouncement(final long authorCompanyId, final QueryDto query, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AppAnnouncementResponse>(callBack) {
            @Override
            protected AppAnnouncementResponse doRequestData(ManagedChannel channel) {
                AnnouncementRequest message = AnnouncementRequest.newBuilder()
                        .setAuthorCompanyId(authorCompanyId)
                        .setQuery(query)
                        .build();
                Logger.d(message);
                AppAnnouncementResponse response = null;
                try {
                    response = getUserStub(channel).getAnnouncement(message);
                    Logger.d(response);
                } catch (Exception e) {
                    Logger.e(e, "getAnnouncement");
                }

                return response;
            }
        }.doExecute();
    }


    /**
     * 信息已读
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, MessageReadResponse> messageRead(final long messageId, final int messageType, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, MessageReadResponse>(callBack) {
            @Override
            protected MessageReadResponse doRequestData(ManagedChannel channel) {
                MessageReadRequest message = MessageReadRequest.newBuilder()
                        .addMessageId(messageId)
                        .setMessageType(messageType)
                        .build();
                Logger.d(message);
                MessageReadResponse response = null;
                try {
                    response = getUserStub(channel).messageRead(message);
                    Logger.d(response);
                } catch (Exception e) {
                    Logger.e(e, "messageRead");
                }

                return response;
            }
        }.doExecute();
    }

}
