package com.gx.smart.smartoa.data.network.api.interceptor;

import io.grpc.Metadata;

public interface IntercepterConstants {
    String AUTHORIZATION = "authorization";
    String BEARER = "Bearer ";
    String SYS_TENANT_NO = "sys_tenant_no";
    String EMPLOYEE_ID = "employee_id";

    Metadata.Key<String> AUTHORIZATION_KEY = Metadata.Key.of(AUTHORIZATION, Metadata.ASCII_STRING_MARSHALLER);
    Metadata.Key<String> META_KEY_SYS_TENANT_NO = Metadata.Key.of(SYS_TENANT_NO, Metadata.ASCII_STRING_MARSHALLER);
    Metadata.Key<String> META_KEY_EMPLOYEE_ID = Metadata.Key.of(EMPLOYEE_ID, Metadata.ASCII_STRING_MARSHALLER);
}
