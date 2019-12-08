package com.gx.smart.smartoa.data.network.api;

import android.util.Log;

import com.gx.smart.smartoa.data.network.api.base.CallBack;
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask;
import com.gx.wisestone.work.app.grpc.attendance.AppAttendanceInterfaceGrpc;
import com.gx.wisestone.work.app.grpc.attendance.AttendanceRequest;
import com.gx.wisestone.work.app.grpc.common.CommonResponse;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;


public class AppAttendanceService {

    //25秒，网络请求超时
    public static final int TIMEOUT_NETWORK = 25;

    private static AppAttendanceService UserCenterClient;

    private AppAttendanceService() {

    }

    public static AppAttendanceService getInstance() {
        if (UserCenterClient == null) {
            UserCenterClient = new AppAttendanceService();
        }
        return UserCenterClient;
    }

    /**
     * 获取UserCenterstub  设置超时时间  8秒
     *
     * @param channel
     * @return
     */
    public AppAttendanceInterfaceGrpc.AppAttendanceInterfaceBlockingStub getAppAttendance(ManagedChannel channel) {
        return AppAttendanceInterfaceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(TIMEOUT_NETWORK, TimeUnit.SECONDS);

    }


    /**
     * 广告图 所有app一样
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, CommonResponse> attendance(String latitude, String longitude,
                                                                  String address, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                AttendanceRequest message = AttendanceRequest.newBuilder()
                        .setLatitude(latitude)
                        .setLongitude(longitude)
                        .setAddress(address)
                        .build();
                CommonResponse response = null;
                try {
                    response = getAppAttendance(channel).attendance(message);
                } catch (Exception e) {
                    Log.i("UserCenter_gRpc", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }

}
