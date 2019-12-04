package com.gx.smart.smartoa.data.network.api;

import android.util.Log;

import com.gx.smart.smartoa.data.network.api.base.CallBack;
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask;
import com.gx.wisestone.work.app.grpc.common.CommonResponse;
import com.gx.wisestone.work.app.grpc.structure.AddCustomerRecordRequest;
import com.gx.wisestone.work.app.grpc.structure.AppChangeSwitchRequest;
import com.gx.wisestone.work.app.grpc.structure.AppGetBuildingInfoRequest;
import com.gx.wisestone.work.app.grpc.structure.ApplyEmployeeRequest;
import com.gx.wisestone.work.app.grpc.structure.GetDepartmentRequest;
import com.gx.wisestone.work.app.grpc.structure.StructureInterfaceGrpc;
import com.gx.wisestone.work.app.grpc.structure.SysTenantListRequest;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;


public class AppStructureService {

    //25秒，网络请求超时
    public static final int TIMEOUT_NETWORK = 25;

    private static AppStructureService UserCenterClient;

    private AppStructureService() {

    }

    public static AppStructureService getInstance() {
        if (UserCenterClient == null) {
            UserCenterClient = new AppStructureService();
        }
        return UserCenterClient;
    }

    /**
     * 获取UserCenterstub  设置超时时间  8秒
     *
     * @param channel
     * @return
     */
    public static StructureInterfaceGrpc.StructureInterfaceBlockingStub getUserStub(ManagedChannel channel) {
        return StructureInterfaceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(TIMEOUT_NETWORK, TimeUnit.SECONDS);

    }


    /**
     * 广告图 所有app一样
     *
     * @return callBack返回值
     */
    public static GrpcAsyncTask<String, Void, CommonResponse> getSysTenantList(CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                SysTenantListRequest message = SysTenantListRequest.newBuilder()
                        .build();
                CommonResponse response = null;
                try {
                    response = getUserStub(channel).getSysTenantList(message);
                } catch (Exception e) {
                    Log.i("UserCenter_gRpc", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }

    /**
     * 轮播图 ，每个小区不同
     *
     * @param sysTenantNo 小区Id
     * @return callBack返回值
     */
    public static GrpcAsyncTask<String, Void, CommonResponse> getBuildingInfo(final int sysTenantNo, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                AppGetBuildingInfoRequest message = AppGetBuildingInfoRequest.newBuilder()
                        .build();
                CommonResponse response = null;
                try {
                    response = getUserStub(channel).getBuildingInfo(message);
                } catch (Exception e) {
                    Log.i("UserCenter_gRpc", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }


    //获取房间信息

    /**
     * 轮播图 ，每个小区不同
     *
     * @param sysTenantNo 小区Id
     * @return callBack返回值
     */
    public static GrpcAsyncTask<String, Void, CommonResponse> getDepartment(final int sysTenantNo, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                GetDepartmentRequest message = GetDepartmentRequest.newBuilder()
                        .build();
                CommonResponse response = null;
                try {
                    response = getUserStub(channel).getDepartment(message);
                } catch (Exception e) {
                    Log.i("UserCenter_gRpc", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }

    /**
     * 轮播图 ，每个小区不同
     *
     * @param sysTenantNo 小区Id
     * @return callBack返回值
     */
    public static GrpcAsyncTask<String, Void, CommonResponse> applyEmployee(final int sysTenantNo, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                ApplyEmployeeRequest message = ApplyEmployeeRequest.newBuilder()
                        .build();
                CommonResponse response = null;
                try {
                    response = getUserStub(channel).applyEmployee(message);
                } catch (Exception e) {
                    Log.i("UserCenter_gRpc", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }

    /**
     * 轮播图 ，每个小区不同
     *
     * @param sysTenantNo 小区Id
     * @return callBack返回值
     */
    public static GrpcAsyncTask<String, Void, CommonResponse> appChangeSwitch(final int sysTenantNo, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                AppChangeSwitchRequest message = AppChangeSwitchRequest.newBuilder()
                        .build();
                CommonResponse response = null;
                try {
                    response = getUserStub(channel).appChangeSwitch(message);
                } catch (Exception e) {
                    Log.i("UserCenter_gRpc", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }


    /**
     * 添加400电话记录  不测
     *
     * @param sysTenantNo 小区Id
     * @return callBack返回值
     */
    public static GrpcAsyncTask<String, Void, CommonResponse> appAddCustomerRecord(final int sysTenantNo, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                AddCustomerRecordRequest message = AddCustomerRecordRequest.newBuilder()
                        .build();
                CommonResponse response = null;
                try {
                    response = getUserStub(channel).appAddCustomerRecord(message);
                } catch (Exception e) {
                    Log.i("UserCenter_gRpc", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }

}
