package com.gx.smart.smartoa.data.network.api.interceptor;


import com.gx.smart.smartoa.data.network.AppConfig;

public class WsProvider implements WsClientCall.Provider {

    @Override
    public String token() {
        return AppConfig.loginToken;
    }

    @Override
    public Integer sysTenantNo() {
        return AppConfig.currentSysTenantNo;
    }

}
