package com.gx.smart.smartoa.data.network.api;

import com.gx.smart.smartoa.data.network.api.base.CallBack;
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask;
import com.gx.wisestone.core.grpc.lib.common.QueryDto;
import com.gx.wisestone.work.app.grpc.common.CommonResponse;
import com.gx.wisestone.work.app.grpc.repair.AppRepairInterfaceGrpc;
import com.gx.wisestone.work.app.grpc.repair.QueryMyRepairRequest;
import com.gx.wisestone.work.app.grpc.repair.RepairCommonResponse;
import com.gx.wisestone.work.app.grpc.repair.RepairRequest;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;


public class AppRepairService {

    //25秒，网络请求超时
    public static final int TIMEOUT_NETWORK = 25;

    private static AppRepairService UserCenterClient;

    private AppRepairService() {

    }

    public static AppRepairService getInstance() {
        if (UserCenterClient == null) {
            UserCenterClient = new AppRepairService();
        }
        return UserCenterClient;
    }

    /**
     * 获取UserCenterstub  设置超时时间  8秒
     *
     * @param channel
     * @return
     */
    public AppRepairInterfaceGrpc.AppRepairInterfaceBlockingStub getAppRepairStub(ManagedChannel channel) {
        return AppRepairInterfaceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(TIMEOUT_NETWORK, TimeUnit.SECONDS);

    }


    /**
     * 添加报修
     * string content = 7;  //内容
     * int32 type = 8;  //1.设备顺坏2.办公绿化3.公共卫生
     * string address = 10;  //地址
     * string employee_phone = 11;  //联系方式
     * bytes image_bytes =14;//图片数据
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, CommonResponse> addRepair(String content,
                                                                 int type,
                                                                 String address,
                                                                 String employee_phone,
                                                                 List<String> images,
                                                                 CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                RepairRequest message = RepairRequest.newBuilder()
                        .setContent(content)
                        .setType(type)
                        .setAddress(address)
                        .setEmployeePhone(employee_phone)
                        .addAllImageUrl(images)
                        .build();
                Logger.d(message);
                CommonResponse response = null;
                try {
                    response = getAppRepairStub(channel).addRepair(message);
                    Logger.d(response);
                } catch (Exception e) {
                    Logger.e(e, "addRepair");
                }

                return response;
            }
        }.doExecute();
    }

    /**
     * 查询报修
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, RepairCommonResponse> queryMyRepair(QueryDto query, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, RepairCommonResponse>(callBack) {
            @Override
            protected RepairCommonResponse doRequestData(ManagedChannel channel) {
                QueryMyRepairRequest message = QueryMyRepairRequest.newBuilder()
                        .setQuery(query)
                        .build();
                Logger.d(message);
                RepairCommonResponse response = null;
                try {
                    response = getAppRepairStub(channel).queryMyRepair(message);
                    Logger.d(response);
                } catch (Exception e) {
                    Logger.e(e, "queryMyRepair");
                }

                return response;
            }
        }.doExecute();
    }


}
