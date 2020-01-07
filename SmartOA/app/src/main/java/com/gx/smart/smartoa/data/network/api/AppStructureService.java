package com.gx.smart.smartoa.data.network.api;


import com.google.protobuf.ByteString;
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
import com.orhanobut.logger.Logger;

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
    public static StructureInterfaceGrpc.StructureInterfaceBlockingStub getStructureStub(ManagedChannel channel) {
        return StructureInterfaceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(TIMEOUT_NETWORK, TimeUnit.SECONDS);

    }


    /**
     * 获取所有办公楼
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, CommonResponse> getSysTenantList(CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                SysTenantListRequest message = SysTenantListRequest.newBuilder()
                        .build();
                Logger.d(message);
                CommonResponse response = null;
                try {
                    response = getStructureStub(channel).getSysTenantList(message);
                    Logger.d(response);
                } catch (Exception e) {
                    Logger.e("AppStructureService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }


    /**
     * 获取所有办公楼
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, CommonResponse> getSysTenantList(String name, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                SysTenantListRequest message = SysTenantListRequest.newBuilder()
                        .setName(name)
                        .build();
                Logger.d(message);
                CommonResponse response = null;
                try {
                    response = getStructureStub(channel).getSysTenantList(message);
                    Logger.d(response);
                } catch (Exception e) {
                    Logger.e(e, "getSysTenantList");
                }

                return response;
            }
        }.doExecute();
    }

    /**
     * 轮播图 ，每个小区不同
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, CommonResponse> getBuildingInfo(CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                AppGetBuildingInfoRequest message = AppGetBuildingInfoRequest.newBuilder()
                        .build();
                CommonResponse response = null;
                try {
                    response = getStructureStub(channel).getBuildingInfo(message);
                } catch (Exception e) {
                    Logger.e("AppStructureService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }


    //获取房间信息

    /**
     * 轮播图 ，每个小区不同
     *
     * @param companyId 小区Id
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, CommonResponse> getDepartment(final long companyId, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                GetDepartmentRequest message = GetDepartmentRequest.newBuilder()
                        .setCompanyStructureId(companyId)
                        .build();
                CommonResponse response = null;
                try {
                    response = getStructureStub(channel).getDepartment(message);
                } catch (Exception e) {
                    Logger.e("AppStructureService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }

    /**
     * 轮播图 ，每个小区不同
     * string name = 1;
     * string mobile = 2;
     * bytes image_bytes =3;//工牌名片
     * int64 company_id = 4; //申请哪个房间
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, CommonResponse> applyEmployee(
            String name, String mobile, ByteString image, final long companyId,
            CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                ApplyEmployeeRequest message;
                if (image != null) {
                    message = ApplyEmployeeRequest.newBuilder()
                            .setName(name)
                            .setMobile(mobile)
                            .setImageBytes(image)
                            .setCompanyStructureId(companyId)
                            .build();
                } else {
                    message = ApplyEmployeeRequest.newBuilder()
                            .setName(name)
                            .setMobile(mobile)
                            .setCompanyStructureId(companyId)
                            .build();
                }
                Logger.d(message);
                CommonResponse response = null;
                try {
                    response = getStructureStub(channel).applyEmployee(message);
                    Logger.d(response);
                } catch (Exception e) {
                    Logger.e("AppStructureService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }

    /**
     * 轮播图 ，每个小区不同
     * string switch_type = 1; //1可视对讲
     * string switch_value = 2;//开关 1开2关
     * int64 structure_id = 3;
     * int64 owner_id = 4;
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, CommonResponse> appChangeSwitch(String switch_type,
                                                                              String switch_value,
                                                                              final int structure_id, final int owner_id, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                AppChangeSwitchRequest message = AppChangeSwitchRequest.newBuilder()
                        .setSwitchType(switch_value)
                        .setSwitchValue(switch_value)
                        .setStructureId(structure_id)
                        .setOwnerId(owner_id)
                        .build();
                CommonResponse response = null;
                try {
                    response = getStructureStub(channel).appChangeSwitch(message);
                } catch (Exception e) {
                    Logger.e("AppStructureService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }


    /**
     * 添加400电话记录  不测
     *
     * @param mobile 小区Id
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, CommonResponse> appAddCustomerRecord(final String mobile, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, CommonResponse>(callBack) {
            @Override
            protected CommonResponse doRequestData(ManagedChannel channel) {
                AddCustomerRecordRequest message = AddCustomerRecordRequest.newBuilder()
                        .setTargetMobile(mobile)
                        .build();
                CommonResponse response = null;
                try {
                    response = getStructureStub(channel).appAddCustomerRecord(message);
                } catch (Exception e) {
                    Logger.e("AppStructureService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }

}
