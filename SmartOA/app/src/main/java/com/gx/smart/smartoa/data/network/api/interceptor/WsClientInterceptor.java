package com.gx.smart.smartoa.data.network.api.interceptor;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.MethodDescriptor;

/**
 * @Author niling on 2019/7/22
 */
public class WsClientInterceptor implements ClientInterceptor {
    private WsClientCall.Provider provider;

    public WsClientInterceptor(WsClientCall.Provider provider) {
        this.provider = provider;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return new WsClientCall<>(next.newCall(method, callOptions), provider);
    }

}
