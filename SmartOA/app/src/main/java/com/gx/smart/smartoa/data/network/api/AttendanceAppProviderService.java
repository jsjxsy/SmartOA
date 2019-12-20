package com.gx.smart.smartoa.data.network.api;

import com.gx.smart.smartoa.data.network.ApiConfig;
import com.gx.smart.smartoa.data.network.api.base.CallBack;
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask;
import com.gx.wisestone.work.grpc.ds.attendanceapp.AttendanceAppProviderGrpc;
import com.gx.wisestone.work.grpc.ds.attendanceapp.getEmployeeDayRecordResp;
import com.gx.wisestone.work.grpc.ds.attendanceapp.getEmployeeRecordDto;
import com.gx.wisestone.work.grpc.ds.attendanceapp.getEmployeeRecordListQueryReq;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;

/**
 * @author xiaosy
 * @create 2019-12-19
 * @Describe
 **/
public class AttendanceAppProviderService {  //25秒，网络请求超时
    public static final int TIMEOUT_NETWORK = 25;

    private static AttendanceAppProviderService UserCenterClient;

    private AttendanceAppProviderService() {

    }

    public static AttendanceAppProviderService getInstance() {
        if (UserCenterClient == null) {
            UserCenterClient = new AttendanceAppProviderService();
        }
        return UserCenterClient;
    }

    /**
     * 获取UserCenterstub  设置超时时间  8秒
     *
     * @param channel
     * @return
     */
    private AttendanceAppProviderGrpc.AttendanceAppProviderBlockingStub getAppAttendance(ManagedChannel channel) {
        return AttendanceAppProviderGrpc.newBlockingStub(channel)
                .withDeadlineAfter(TIMEOUT_NETWORK, TimeUnit.SECONDS);

    }


    /**
     * 查询员工今日考勤记录
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, getEmployeeDayRecordResp> getEmployeeDayRecord(CallBack callBack) {
        return new GrpcAsyncTask<String, Void, getEmployeeDayRecordResp>(callBack) {
            @Override
            protected getEmployeeDayRecordResp doRequestData(ManagedChannel channel) {
                getEmployeeRecordDto message = getEmployeeRecordDto.newBuilder()
                        .build();
                getEmployeeDayRecordResp response = null;
                try {
                    response = getAppAttendance(channel).getEmployeeDayRecord(message);
                } catch (Exception e) {
                    Logger.e("AppAttendanceService", e.getMessage());
                }

                return response;
            }
        }.setPort(ApiConfig.ATTENDANCE_SERVICE_PORT).doExecute();
    }


    /**
     * 查询员工今日考勤记录
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, getEmployeeDayRecordResp> getEmployeeRecordList(CallBack callBack) {
        return new GrpcAsyncTask<String, Void, getEmployeeDayRecordResp>(callBack) {
            @Override
            protected getEmployeeDayRecordResp doRequestData(ManagedChannel channel) {
                getEmployeeRecordListQueryReq message = getEmployeeRecordListQueryReq.newBuilder()
                        .build();
                getEmployeeDayRecordResp response = null;
                try {
                    response = getAppAttendance(channel).getEmployeeRecordList(message);
                } catch (Exception e) {
                    Logger.e("AppAttendanceService", e.getMessage());
                }

                return response;
            }
        }.setPort(ApiConfig.ATTENDANCE_SERVICE_PORT).doExecute();
    }
}
