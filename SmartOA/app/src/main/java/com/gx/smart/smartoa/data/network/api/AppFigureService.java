package com.gx.smart.smartoa.data.network.api;

import android.util.Log;

import com.gx.smart.smartoa.data.network.api.base.CallBack;
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask;
import com.gx.wisestone.work.app.grpc.appfigure.AppFigureInterfaceGrpc;
import com.gx.wisestone.work.app.grpc.appfigure.BannerFigureRequest;
import com.gx.wisestone.work.app.grpc.appfigure.CarouselFigureRequest;
import com.gx.wisestone.work.app.grpc.appfigure.ImagesResponse;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;


public class AppFigureService {

    //25秒，网络请求超时
    public static final int TIMEOUT_NETWORK = 25;

    private static AppFigureService UserCenterClient;

    private AppFigureService() {

    }

    public static AppFigureService getInstance() {
        if (UserCenterClient == null) {
            UserCenterClient = new AppFigureService();
        }
        return UserCenterClient;
    }

    /**
     * 获取UserCenterstub  设置超时时间  8秒
     *
     * @param channel
     * @return
     */
    public static AppFigureInterfaceGrpc.AppFigureInterfaceBlockingStub getUserStub(ManagedChannel channel) {
        return AppFigureInterfaceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(TIMEOUT_NETWORK, TimeUnit.SECONDS);

    }


    /**
     * 广告图 所有app一样
     *
     * @return callBack返回值
     */
    public static GrpcAsyncTask<String, Void, ImagesResponse> bannerFigure(CallBack callBack) {
        return new GrpcAsyncTask<String, Void, ImagesResponse>(callBack) {
            @Override
            protected ImagesResponse doRequestData(ManagedChannel channel) {
                BannerFigureRequest message = BannerFigureRequest.newBuilder()
                        .build();
                ImagesResponse response = null;
                try {
                    response = getUserStub(channel).bannerFigure(message);
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
    public static GrpcAsyncTask<String, Void, ImagesResponse> carouselFigure(final int sysTenantNo, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, ImagesResponse>(callBack) {
            @Override
            protected ImagesResponse doRequestData(ManagedChannel channel) {
                CarouselFigureRequest message = CarouselFigureRequest.newBuilder()
                        .setSysTenantNo(sysTenantNo)
                        .build();
                ImagesResponse response = null;
                try {
                    response = getUserStub(channel).carouselFigure(message);
                } catch (Exception e) {
                    Log.i("UserCenter_gRpc", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }


}
