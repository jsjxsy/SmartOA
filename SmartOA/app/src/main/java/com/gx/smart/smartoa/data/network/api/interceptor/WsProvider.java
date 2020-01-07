package com.gx.smart.smartoa.data.network.api.interceptor;


import com.blankj.utilcode.util.SPUtils;
import com.gx.smart.smartoa.data.network.AppConfig;

public class WsProvider implements WsClientCall.Provider {

    @Override
    public String token() {
        return SPUtils.getInstance().getString(AppConfig.LOGIN_TOKEN, "");
    }

    @Override
    public Integer sysTenantNo() {
        return SPUtils.getInstance().getInt(AppConfig.SYS_TENANT_NO, 0);
    }

    public Long employeeId() {
        return SPUtils.getInstance().getLong(AppConfig.EMPLOYEE_ID, 0L);
    }

}
