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
        int sysTenantNo = SPUtils.getInstance().getInt(AppConfig.BUILDING_SYS_TENANT_NO, 0);
        if (sysTenantNo == 0) {
            sysTenantNo = SPUtils.getInstance().getInt(AppConfig.COMPANY_SYS_TENANT_NO, 0);
        }
        return sysTenantNo;
    }

//    public Long employeeId() {
//        int buildingSysTenantNo = SPUtils.getInstance().getInt(AppConfig.BUILDING_SYS_TENANT_NO, 0);
//        int companySysTenantNo = SPUtils.getInstance().getInt(AppConfig.COMPANY_SYS_TENANT_NO, 0);
//        long employeeId = 0L;
//        if (buildingSysTenantNo == companySysTenantNo) {
//            employeeId = SPUtils.getInstance().getLong(AppConfig.EMPLOYEE_ID, 0L);
//        }
//        return employeeId;
//    }

}
