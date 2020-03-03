package com.gx.smart.smartoa.data.network.api;

import com.google.protobuf.ByteString;
import com.gx.smart.common.ApiConfig;
import com.gx.smart.lib.http.base.CallBack;
import com.gx.smart.lib.http.base.GrpcAsyncTask;
import com.gx.wisestone.upload.grpc.images.AdminImageProviderGrpc;
import com.gx.wisestone.upload.grpc.images.AdminImagesResponse;
import com.gx.wisestone.upload.grpc.images.UploadByByteRequest;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;

/**
 * @author xiaosy
 * @create 2019-12-20
 * @Describe
 **/
public class AdminImageProviderService {
    //25秒，网络请求超时
    public static final int TIMEOUT_NETWORK = 25;

    private static AdminImageProviderService UserCenterClient;

    private AdminImageProviderService() {

    }

    public static AdminImageProviderService getInstance() {
        if (UserCenterClient == null) {
            UserCenterClient = new AdminImageProviderService();
        }
        return UserCenterClient;
    }

    /**
     * 获取UserCenterstub  设置超时时间  8秒
     *
     * @param channel
     * @return
     */
    public AdminImageProviderGrpc.AdminImageProviderBlockingStub getAdminImage(ManagedChannel channel) {
        return AdminImageProviderGrpc.newBlockingStub(channel)
                .withDeadlineAfter(TIMEOUT_NETWORK, TimeUnit.SECONDS);

    }


    /**
     * 广告图 所有app一样
     *
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AdminImagesResponse> uploadByByte(String prefix, String fileName,
                                                                         ByteString image, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AdminImagesResponse>(callBack) {
            @Override
            protected AdminImagesResponse doRequestData(ManagedChannel channel) {
                UploadByByteRequest message = UploadByByteRequest.newBuilder()
                        .setPrefix(prefix)
                        .setFileName(fileName)
                        .setImageBytes(image)
                        .build();
                Logger.d(message);
                AdminImagesResponse response = null;
                try {
                    response = getAdminImage(channel).uploadByByte(message);
                    Logger.d(response);
                } catch (Exception e) {
                    Logger.e(e, "uploadByByte");
                }

                return response;
            }
        }.setHost(ApiConfig.UPLOAD_IMAGE_SERVER_URL, ApiConfig.UPLOAD_IMAGE_SERVER_PORT).doExecute();
    }
}