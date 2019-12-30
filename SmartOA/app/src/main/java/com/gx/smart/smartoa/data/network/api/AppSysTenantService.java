package com.gx.smart.smartoa.data.network.api;

import android.util.Log;

import com.gx.smart.smartoa.data.network.api.base.CallBack;
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask;
import com.gx.wisestone.work.app.grpc.systenant.AppBuildingResponse;
import com.gx.wisestone.work.app.grpc.systenant.AppCompanyResponse;
import com.gx.wisestone.work.app.grpc.systenant.AppGetBuildingInfoRequest;
import com.gx.wisestone.work.app.grpc.systenant.AppSysTenantGrpc;
import com.gx.wisestone.work.app.grpc.systenant.SysTenantListRequest;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;


public class AppSysTenantService {

    //25秒，网络请求超时
    public static final int TIMEOUT_NETWORK = 25;

    private static AppSysTenantService UserCenterClient;

    private AppSysTenantService() {

    }

    public static AppSysTenantService getInstance() {
        if (UserCenterClient == null) {
            UserCenterClient = new AppSysTenantService();
        }
        return UserCenterClient;
    }

    /**
     * 获取UserCenterstub  设置超时时间  8秒
     *
     * @param channel
     * @return
     */
    public AppSysTenantGrpc.AppSysTenantBlockingStub getAppSysTenantStub(ManagedChannel channel) {
        return AppSysTenantGrpc.newBlockingStub(channel)
                .withDeadlineAfter(TIMEOUT_NETWORK, TimeUnit.SECONDS);

    }


    /**
     * 广告图 所有app一样
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AppBuildingResponse> getSysTenantList(String name, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AppBuildingResponse>(callBack) {
            @Override
            protected AppBuildingResponse doRequestData(ManagedChannel channel) {
                SysTenantListRequest message = SysTenantListRequest.newBuilder()
                        .build();
                Logger.d(message);
                AppBuildingResponse response = null;
                try {
                    response = getAppSysTenantStub(channel).getSysTenantList(message);
                    Logger.d(response);
                } catch (Exception e) {
                    Logger.e("UserCenter_gRpc", e.getMessage());
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
    public GrpcAsyncTask<String, Void, AppCompanyResponse> getBuildingInfo(final int sysTenantNo, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AppCompanyResponse>(callBack) {
            @Override
            protected AppCompanyResponse doRequestData(ManagedChannel channel) {
                AppGetBuildingInfoRequest message = AppGetBuildingInfoRequest.newBuilder()
                        .setSysTenantNo(sysTenantNo)
                        .build();
                Logger.d(message);
                AppCompanyResponse response = null;
                try {
                    response = getAppSysTenantStub(channel).getBuildingInfo(message);
                    Logger.d(message);
                } catch (Exception e) {
                    Log.i("UserCenter_gRpc", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }


}
