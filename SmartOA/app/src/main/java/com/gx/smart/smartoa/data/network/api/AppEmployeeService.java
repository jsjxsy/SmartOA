package com.gx.smart.smartoa.data.network.api;

import com.gx.smart.smartoa.data.network.api.base.CallBack;
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask;
import com.gx.wisestone.work.app.grpc.common.CommonResponse;
import com.gx.wisestone.work.app.grpc.employee.AppEmployeeInterfaceGrpc;
import com.gx.wisestone.work.app.grpc.employee.AppMyCompanyResponse;
import com.gx.wisestone.work.app.grpc.employee.CancelCompanyApplyRequest;
import com.gx.wisestone.work.app.grpc.employee.CancelCompanyBindRequest;
import com.gx.wisestone.work.app.grpc.employee.MyCompanyRequest;
import com.orhanobut.logger.Logger;

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
                Logger.d(message);
                CommonResponse response = null;
                try {
                    response = getAppEmployeeStub(channel).cancelCompanyBind(message);
                    Logger.d(response);
                } catch (Exception e) {
                    Logger.e(e, "cancelCompanyBind");
                }

                return response;
            }
        }.doExecute();
    }



    /**
     * 我的企业
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AppMyCompanyResponse> myCompany(CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AppMyCompanyResponse>(callBack) {
            @Override
            protected AppMyCompanyResponse doRequestData(ManagedChannel channel) {
                MyCompanyRequest message = MyCompanyRequest.newBuilder()
                        .build();
                Logger.d(message);
                AppMyCompanyResponse response = null;
                try {
                    response = getAppEmployeeStub(channel).myCompany(message);
                    Logger.d(response);
                } catch (Exception e) {
                    Logger.e( e, "myCompany");
                }

                return response;
            }
        }.doExecute();
    }



    /**
     * 撤销企业申请
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, CommonResponse> cancelCompanyApply(long applyId, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                CancelCompanyApplyRequest message = CancelCompanyApplyRequest.newBuilder()
                        .setApplyId(applyId)
                        .build();
                Logger.d(message);
                CommonResponse response = null;
                try {
                    response = getAppEmployeeStub(channel).cancelCompanyApply(message);
                    Logger.d(response);
                } catch (Exception e) {
                    Logger.e( e, "cancelCompanyApply");
                }

                return response;
            }
        }.doExecute();
    }
}
