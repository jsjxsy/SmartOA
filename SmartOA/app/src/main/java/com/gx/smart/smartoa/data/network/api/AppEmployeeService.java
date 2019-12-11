package com.gx.smart.smartoa.data.network.api;

import android.util.Log;

import com.gx.smart.smartoa.data.network.api.base.CallBack;
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask;
import com.gx.wisestone.work.app.grpc.appfigure.AppFigureInterfaceGrpc;
import com.gx.wisestone.work.app.grpc.appfigure.BannerFigureRequest;
import com.gx.wisestone.work.app.grpc.appfigure.CarouselFigureRequest;
import com.gx.wisestone.work.app.grpc.appfigure.ImagesResponse;
import com.gx.wisestone.work.app.grpc.common.CommonResponse;
import com.gx.wisestone.work.app.grpc.employee.AppEmployeeInterfaceGrpc;
import com.gx.wisestone.work.app.grpc.employee.CancelCompanyBindRequest;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;


public class AppEmployeeService {

    //25秒，网络请求超时
    public static final int TIMEOUT_NETWORK = 25;

    private static AppEmployeeService UserCenterClient;

    private AppEmployeeService() {

    }

    public static AppEmployeeService getInstance() {
        if (UserCenterClient == null) {
            UserCenterClient = new AppEmployeeService();
        }
        return UserCenterClient;
    }

    /**
     * 获取UserCenterstub  设置超时时间  8秒
     *
     * @param channel
     * @return
     */
    public AppEmployeeInterfaceGrpc.AppEmployeeInterfaceBlockingStub getAppEmployeeStub(ManagedChannel channel) {
        return AppEmployeeInterfaceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(TIMEOUT_NETWORK, TimeUnit.SECONDS);

    }


    /**
     * 解绑公司 注意：header添加sys_tenant_no，employee_id
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, CommonResponse> cancelCompanyBind(CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                CancelCompanyBindRequest message = CancelCompanyBindRequest.newBuilder()
                        .build();
                CommonResponse response = null;
                try {
                    response = getAppEmployeeStub(channel).cancelCompanyBind(message);
                } catch (Exception e) {
                    Log.i("UserCenter_gRpc", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }



}
