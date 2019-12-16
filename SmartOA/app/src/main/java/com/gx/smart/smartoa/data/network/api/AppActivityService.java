package com.gx.smart.smartoa.data.network.api;

import android.util.Log;

import com.gx.smart.smartoa.data.network.api.base.CallBack;
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask;
import com.gx.wisestone.core.grpc.lib.common.QueryDto;
import com.gx.wisestone.work.app.grpc.activity.ActivityCommonResponse;
import com.gx.wisestone.work.app.grpc.activity.ActivityQueryRequest;
import com.gx.wisestone.work.app.grpc.activity.ActivityRequest;
import com.gx.wisestone.work.app.grpc.activity.AppActivityApplyDto;
import com.gx.wisestone.work.app.grpc.activity.AppActivityApplyResponse;
import com.gx.wisestone.work.app.grpc.activity.AppActivityInterfaceGrpc;
import com.gx.wisestone.work.app.grpc.activity.AppActivityResponse;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;


public class AppActivityService {

    //25秒，网络请求超时
    public static final int TIMEOUT_NETWORK = 25;

    private static AppActivityService UserCenterClient;

    private AppActivityService() {

    }

    public static AppActivityService getInstance() {
        if (UserCenterClient == null) {
            UserCenterClient = new AppActivityService();
        }
        return UserCenterClient;
    }

    /**
     * 获取UserCenterstub  设置超时时间  8秒
     *
     * @param channel
     * @return
     */
    public AppActivityInterfaceGrpc.AppActivityInterfaceBlockingStub getAppActivityStub(ManagedChannel channel) {
        return AppActivityInterfaceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(TIMEOUT_NETWORK, TimeUnit.SECONDS);

    }


    /**
     * 添加活动
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AppActivityResponse> addActivity(CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AppActivityResponse>(callBack) {
            @Override
            protected AppActivityResponse doRequestData(ManagedChannel channel) {
                ActivityRequest message = ActivityRequest.newBuilder()
                        .build();
                AppActivityResponse response = null;
                try {
                    response = getAppActivityStub(channel).addActivity(message);
                } catch (Exception e) {
                    Log.e("AppActivityService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }

    /**
     * 查询活动列表
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, ActivityCommonResponse> findAllApplyInfos(QueryDto query, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, ActivityCommonResponse>(callBack) {
            @Override
            protected ActivityCommonResponse doRequestData(ManagedChannel channel) {
                ActivityQueryRequest message = ActivityQueryRequest.newBuilder()
                        .setQuery(query)
                        .build();
                ActivityCommonResponse response = null;
                try {
                    response = getAppActivityStub(channel).findAllApplyInfos(message);
                } catch (Exception e) {
                    Log.e("AppActivityService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }


    /**
     * 我的申请列表
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, ActivityCommonResponse> findMyApplyInfos(QueryDto query, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, ActivityCommonResponse>(callBack) {
            @Override
            protected ActivityCommonResponse doRequestData(ManagedChannel channel) {
                ActivityQueryRequest message = ActivityQueryRequest.newBuilder()
                        .setQuery(query)
                        .build();
                ActivityCommonResponse response = null;
                try {
                    response = getAppActivityStub(channel).findMyApplyInfos(message);
                } catch (Exception e) {
                    Log.e("AppActivityService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }


    /**
     * 活动报名
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AppActivityApplyResponse> apply(CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AppActivityApplyResponse>(callBack) {
            @Override
            protected AppActivityApplyResponse doRequestData(ManagedChannel channel) {
                AppActivityApplyDto message = AppActivityApplyDto.newBuilder()
                        .build();
                AppActivityApplyResponse response = null;
                try {
                    response = getAppActivityStub(channel).apply(message);
                } catch (Exception e) {
                    Log.e("AppActivityService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }

    /**
     * 取消报名
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AppActivityApplyResponse> cancelApply(CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AppActivityApplyResponse>(callBack) {
            @Override
            protected AppActivityApplyResponse doRequestData(ManagedChannel channel) {
                AppActivityApplyDto message = AppActivityApplyDto.newBuilder()
                        .build();
                AppActivityApplyResponse response = null;
                try {
                    response = getAppActivityStub(channel).cancelApply(message);
                } catch (Exception e) {
                    Log.e("AppActivityService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }

    /**
     * 查询报名信息
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AppActivityApplyResponse> findApplyInfo(long activityId, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AppActivityApplyResponse>(callBack) {
            @Override
            protected AppActivityApplyResponse doRequestData(ManagedChannel channel) {
                AppActivityApplyDto message = AppActivityApplyDto.newBuilder()
                        .setActivityId(activityId)
                        .build();
                AppActivityApplyResponse response = null;
                try {
                    response = getAppActivityStub(channel).findApplyInfo(message);
                } catch (Exception e) {
                    Log.e("AppActivityService", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }


}
