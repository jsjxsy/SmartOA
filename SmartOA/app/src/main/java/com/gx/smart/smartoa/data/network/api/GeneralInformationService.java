package com.gx.smart.smartoa.data.network.api;



import com.gx.smart.smartoa.data.network.ApiConfig;
import com.gx.smart.smartoa.data.network.api.base.CallBack;
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask;
import com.gx.wisestone.core.grpc.lib.generalinformation.GeneralInformationServiceGrpc;
import com.gx.wisestone.core.grpc.lib.generalinformation.WeatherInformationResponse;
import com.gx.wisestone.core.grpc.lib.generalinformation.WeatherRequest;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;

public class GeneralInformationService {
    public static final int TIMEOUT_NETWORK = 25;
    private static GeneralInformationService GeneralInfoRPCClient;

    private GeneralInformationService() {

    }

    public static GeneralInformationService getInstance() {
        if (GeneralInfoRPCClient == null) {
            GeneralInfoRPCClient = new GeneralInformationService();
        }
        return GeneralInfoRPCClient;
    }

    /**
     * 获取GeneralInfoRPCClientStub  设置超时时间  8秒
     *
     * @param channel
     * @return
     */
    public static GeneralInformationServiceGrpc.GeneralInformationServiceBlockingStub GeneralInfoRPCClientStub(ManagedChannel channel) {
        return GeneralInformationServiceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(TIMEOUT_NETWORK, TimeUnit.SECONDS);

    }


    /**
     * 获取天气信息
     *
     * @param province 省份
     * @param city     城市
     * @param area     区
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, WeatherInformationResponse> getWeatherInfo(final String province, final String city, final String area, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, WeatherInformationResponse>(callBack) {
            @Override
            protected WeatherInformationResponse doRequestData(ManagedChannel channel) {
                WeatherRequest message = WeatherRequest.newBuilder()
                        .setProvince(province)
                        .setCity(city)
                        .setArea(area)
                        .build();

                WeatherInformationResponse response = null;
                try {
                    response = GeneralInfoRPCClientStub(channel).getWeatherInfo(message);
                } catch (Exception e) {
                    Logger.e(e,"getWeatherInfo");
                }

                return response;
            }
        }.setPort(ApiConfig.GENERAL_INFO_SERVICE_PORT).doExecute();
    }

}



