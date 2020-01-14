package com.gx.smart.smartoa.data.network.api.interceptor;

import com.orhanobut.logger.Logger;

import io.grpc.ClientCall;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;

public class WsClientCall<ReqT, RespT> extends ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT> {
    private Provider provider;

    protected WsClientCall(ClientCall delegate, Provider provider) {
        super(delegate);
        this.provider = provider;
    }

    @Override
    public void start(Listener responseListener, Metadata headers) {
        if (null != provider) {
            String token = provider.token();
            Integer sysTenantNo = provider.sysTenantNo();
            Long employeeId = provider.employeeId();
            String wsVer = provider.wsVer();
            if (null != token && token.length() > 0) {
                headers.put(IntercepterConstants.AUTHORIZATION_KEY, IntercepterConstants.BEARER + token);
            }
            if (null != sysTenantNo) {
                headers.put(IntercepterConstants.META_KEY_SYS_TENANT_NO, String.valueOf(sysTenantNo));
            }
            if (null != employeeId) {
                headers.put(IntercepterConstants.META_KEY_EMPLOYEE_ID, String.valueOf(employeeId));
            }
            if (null != wsVer) {
                headers.put(IntercepterConstants.META_KEY_WS_VER, wsVer + "");
            }

        }
        Logger.d("head request: " + headers.toString());
        super.start(new WsClientCallListener(responseListener), headers);
    }

    public interface Provider {
        String token();

        Integer sysTenantNo();

        Long employeeId();

        String wsVer();
    }
}
