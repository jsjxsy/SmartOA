package com.gx.smart.smartoa.data.network.api;

import com.gx.smart.smartoa.data.network.ApiConfig;
import com.gx.smart.smartoa.data.network.api.base.CallBack;
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask;
import com.gx.wisestone.work.grpc.ds.accessrecord.AccessRecordProviderGrpc;
import com.gx.wisestone.work.grpc.ds.accessrecord.AccessRecordReportedDto;
import com.gx.wisestone.work.grpc.ds.accessrecord.AccessRecordReportedResp;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;

/**
 * @author xiaosy
 * @create 2019-12-19
 * @Describe
 **/
public class AccessRecordProviderService {
    //25秒，网络请求超时
    public static final int TIMEOUT_NETWORK = 25;

    private static AccessRecordProviderService UserCenterClient;

    private AccessRecordProviderService() {

    }

    public static AccessRecordProviderService getInstance() {
        if (UserCenterClient == null) {
            UserCenterClient = new AccessRecordProviderService();
        }
        return UserCenterClient;
    }

    /**
     * 获取UserCenterstub  设置超时时间  8秒
     *
     * @param channel
     * @return
     */
    public AccessRecordProviderGrpc.AccessRecordProviderBlockingStub getAppAttendance(ManagedChannel channel) {
        return AccessRecordProviderGrpc.newBlockingStub(channel)
                .withDeadlineAfter(TIMEOUT_NETWORK, TimeUnit.SECONDS);

    }


    /**
     * 广告图 所有app一样
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AccessRecordReportedResp> accessRecordReported(String latitude, String longitude,
                                                                                      String address, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AccessRecordReportedResp>(callBack) {
            @Override
            protected AccessRecordReportedResp doRequestData(ManagedChannel channel) {
                AccessRecordReportedDto message = AccessRecordReportedDto.newBuilder()
                        .setLat(latitude)
                        .setLng(longitude)
                        .setAddress(address)
                        .build();
                AccessRecordReportedResp response = null;
                try {
                    response = getAppAttendance(channel).accessRecordReported(message);
                } catch (Exception e) {
                    Logger.e("AppAttendanceService", e.getMessage());
                }

                return response;
            }
        }.setPort(ApiConfig.ATTENDANCE_SERVICE_PORT).doExecute();
    }
}
