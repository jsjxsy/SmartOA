package com.gx.smart.smartoa.data.network.api.interceptor;

import io.grpc.ClientCall;
import io.grpc.ForwardingClientCallListener;

public class WsClientCallListener<RespT> extends ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT> {
    public WsClientCallListener(ClientCall.Listener<RespT> delegate) {
        //客户端 后置
        super(delegate);
    }
}
