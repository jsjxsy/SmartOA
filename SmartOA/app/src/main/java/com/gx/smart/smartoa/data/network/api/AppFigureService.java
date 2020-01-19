package com.gx.smart.smartoa.data.network.api;

import com.gx.smart.smartoa.data.network.api.base.CallBack;
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask;
import com.gx.wisestone.work.app.grpc.appfigure.AppFigureInterfaceGrpc;
import com.gx.wisestone.work.app.grpc.appfigure.BannerFigureRequest;
import com.gx.wisestone.work.app.grpc.appfigure.CarouselFigureRequest;
import com.gx.wisestone.work.app.grpc.appfigure.ImagesResponse;
import com.orhanobut.logger.Logger;

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
    public AppFigureInterfaceGrpc.AppFigureInterfaceBlockingStub getAppFigureStub(ManagedChannel channel) {
        return AppFigureInterfaceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(TIMEOUT_NETWORK, TimeUnit.SECONDS);

    }


    /**
     * 广告图 所有app一样
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, ImagesResponse> bannerFigure(CallBack callBack) {
        return new GrpcAsyncTask<String, Void, ImagesResponse>(callBack) {
            @Override
            protected ImagesResponse doRequestData(ManagedChannel channel) {
                BannerFigureRequest message = BannerFigureRequest.newBuilder()
                        .build();
                Logger.d(message);
                ImagesResponse response = null;
                try {
                    response = getAppFigureStub(channel).bannerFigure(message);
                    Logger.d(response);
                } catch (Exception e) {
                    Logger.e(e, "bannerFigure");
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
    public GrpcAsyncTask<String, Void, ImagesResponse> carouselFigure(CallBack callBack) {
        return new GrpcAsyncTask<String, Void, ImagesResponse>(callBack) {
            @Override
            protected ImagesResponse doRequestData(ManagedChannel channel) {
                CarouselFigureRequest message = CarouselFigureRequest.newBuilder()
                        .build();
                Logger.d(message);
                ImagesResponse response = null;
                try {
                    response = getAppFigureStub(channel).carouselFigure(message);
                    Logger.d(response);
                } catch (Exception e) {
                    Logger.e(e, "carouselFigure");
                }

                return response;
            }
        }.doExecute();
    }


}
