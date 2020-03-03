package com.gx.smart.lib.http.interceptor;

import io.grpc.ClientCall;
import io.grpc.ForwardingClientCallListener;

public class WsClientCallListener<RespT> extends ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT> {
    public WsClientCallListener(ClientCall.Listener<RespT> delegate) {
        //客户端 后置
        super(delegate);
    }
}
