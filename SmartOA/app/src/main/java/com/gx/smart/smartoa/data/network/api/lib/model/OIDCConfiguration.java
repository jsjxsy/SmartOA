package com.gx.smart.smartoa.data.network.api.lib.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;
import java.util.Map;

/**
 * @Author niling on 2019/7/7
 */
public class OIDCConfiguration {

    public Map<String, Object> toMap() {
        String json = toJson();
        return JSON.parseObject(json, Map.class);
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }

    public static OIDCConfiguration parseFromJson(String json) {
        return JSON.parseObject(json, OIDCConfiguration.class);
    }

    @JSONField(name = "issuer")
    private String issuer;

    @JSONField(name = "authorization_endpoint")
    private String authorizationEndpoint;

    @JSONField(name = "token_endpoint")
    private String tokenEndpoint;

    @JSONField(name = "token_introspection_endpoint")
    private String tokenIntrospectionEndpoint;

    @JSONField(name = "userinfo_endpoint")
    private String userinfoEndpoint;

    @JSONField(name = "end_session_endpoint")
    private String logoutEndpoint;

    @JSONField(name = "jwks_uri")
    private String jwksUri;

    @JSONField(name = "check_session_iframe")
    private String checkSessionIframe;

    @JSONField(name = "grant_types_supported")
    private List<String> grantTypesSupported;

    @JSONField(name = "response_types_supported")
    private List<String> responseTypesSupported;

    @JSONField(name = "subject_types_supported")
    private List<String> subjectTypesSupported;

    @JSONField(name = "id_token_signing_alg_values_supported")
    private List<String> idTokenSigningAlgValuesSupported;

    @JSONField(name = "userinfo_signing_alg_values_supported")
    private List<String> userInfoSigningAlgValuesSupported;

    @JSONField(name = "request_object_signing_alg_values_supported")
    private List<String> requestObjectSigningAlgValuesSupported;

    @JSONField(name = "response_modes_supported")
    private List<String> responseModesSupported;

    @JSONField(name = "registration_endpoint")
    private String registrationEndpoint;

    @JSONField(name = "token_endpoint_auth_methods_supported")
    private List<String> tokenEndpointAuthMethodsSupported;

    @JSONField(name = "token_endpoint_auth_signing_alg_values_supported")
    private List<String> tokenEndpointAuthSigningAlgValuesSupported;

    @JSONField(name = "claims_supported")
    private List<String> claimsSupported;

    @JSONField(name = "claim_types_supported")
    private List<String> claimTypesSupported;

    @JSONField(name = "claims_parameter_supported")
    private Boolean claimsParameterSupported;

    @JSONField(name = "scopes_supported")
    private List<String> scopesSupported;

    @JSONField(name = "request_parameter_supported")
    private Boolean requestParameterSupported;

    @JSONField(name = "request_uri_parameter_supported")
    private Boolean requestUriParameterSupported;

    @JSONField(name = "code_challenge_methods_supported")
    private List<String> codeChallengeMethodsSupported;

    @JSONField(name = "tls_client_certificate_bound_access_tokens")
    private Boolean tlsClientCertificateBoundAccessTokens;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public void setAuthorizationEndpoint(String authorizationEndpoint) {
        this.authorizationEndpoint = authorizationEndpoint;
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }

    public void setTokenEndpoint(String tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }

    public String getTokenIntrospectionEndpoint() {
        return tokenIntrospectionEndpoint;
    }

    public void setTokenIntrospectionEndpoint(String tokenIntrospectionEndpoint) {
        this.tokenIntrospectionEndpoint = tokenIntrospectionEndpoint;
    }

    public String getUserinfoEndpoint() {
        return userinfoEndpoint;
    }

    public void setUserinfoEndpoint(String userinfoEndpoint) {
        this.userinfoEndpoint = userinfoEndpoint;
    }

    public String getLogoutEndpoint() {
        return logoutEndpoint;
    }

    public void setLogoutEndpoint(String logoutEndpoint) {
        this.logoutEndpoint = logoutEndpoint;
    }

    public String getJwksUri() {
        return jwksUri;
    }

    public void setJwksUri(String jwksUri) {
        this.jwksUri = jwksUri;
    }

    public String getCheckSessionIframe() {
        return checkSessionIframe;
    }

    public void setCheckSessionIframe(String checkSessionIframe) {
        this.checkSessionIframe = checkSessionIframe;
    }

    public List<String> getGrantTypesSupported() {
        return grantTypesSupported;
    }

    public void setGrantTypesSupported(List<String> grantTypesSupported) {
        this.grantTypesSupported = grantTypesSupported;
    }

    public List<String> getResponseTypesSupported() {
        return responseTypesSupported;
    }

    public void setResponseTypesSupported(List<String> responseTypesSupported) {
        this.responseTypesSupported = responseTypesSupported;
    }

    public List<String> getSubjectTypesSupported() {
        return subjectTypesSupported;
    }

    public void setSubjectTypesSupported(List<String> subjectTypesSupported) {
        this.subjectTypesSupported = subjectTypesSupported;
    }

    public List<String> getIdTokenSigningAlgValuesSupported() {
        return idTokenSigningAlgValuesSupported;
    }

    public void setIdTokenSigningAlgValuesSupported(List<String> idTokenSigningAlgValuesSupported) {
        this.idTokenSigningAlgValuesSupported = idTokenSigningAlgValuesSupported;
    }

    public List<String> getUserInfoSigningAlgValuesSupported() {
        return userInfoSigningAlgValuesSupported;
    }

    public void setUserInfoSigningAlgValuesSupported(List<String> userInfoSigningAlgValuesSupported) {
        this.userInfoSigningAlgValuesSupported = userInfoSigningAlgValuesSupported;
    }

    public List<String> getRequestObjectSigningAlgValuesSupported() {
        return requestObjectSigningAlgValuesSupported;
    }

    public void setRequestObjectSigningAlgValuesSupported(List<String> requestObjectSigningAlgValuesSupported) {
        this.requestObjectSigningAlgValuesSupported = requestObjectSigningAlgValuesSupported;
    }

    public List<String> getResponseModesSupported() {
        return responseModesSupported;
    }

    public void setResponseModesSupported(List<String> responseModesSupported) {
        this.responseModesSupported = responseModesSupported;
    }

    public String getRegistrationEndpoint() {
        return registrationEndpoint;
    }

    public void setRegistrationEndpoint(String registrationEndpoint) {
        this.registrationEndpoint = registrationEndpoint;
    }

    public List<String> getTokenEndpointAuthMethodsSupported() {
        return tokenEndpointAuthMethodsSupported;
    }

    public void setTokenEndpointAuthMethodsSupported(List<String> tokenEndpointAuthMethodsSupported) {
        this.tokenEndpointAuthMethodsSupported = tokenEndpointAuthMethodsSupported;
    }

    public List<String> getTokenEndpointAuthSigningAlgValuesSupported() {
        return tokenEndpointAuthSigningAlgValuesSupported;
    }

    public void setTokenEndpointAuthSigningAlgValuesSupported(List<String> tokenEndpointAuthSigningAlgValuesSupported) {
        this.tokenEndpointAuthSigningAlgValuesSupported = tokenEndpointAuthSigningAlgValuesSupported;
    }

    public List<String> getClaimsSupported() {
        return claimsSupported;
    }

    public void setClaimsSupported(List<String> claimsSupported) {
        this.claimsSupported = claimsSupported;
    }

    public List<String> getClaimTypesSupported() {
        return claimTypesSupported;
    }

    public void setClaimTypesSupported(List<String> claimTypesSupported) {
        this.claimTypesSupported = claimTypesSupported;
    }

    public Boolean getClaimsParameterSupported() {
        return claimsParameterSupported;
    }

    public void setClaimsParameterSupported(Boolean claimsParameterSupported) {
        this.claimsParameterSupported = claimsParameterSupported;
    }

    public List<String> getScopesSupported() {
        return scopesSupported;
    }

    public void setScopesSupported(List<String> scopesSupported) {
        this.scopesSupported = scopesSupported;
    }

    public Boolean getRequestParameterSupported() {
        return requestParameterSupported;
    }

    public void setRequestParameterSupported(Boolean requestParameterSupported) {
        this.requestParameterSupported = requestParameterSupported;
    }

    public Boolean getRequestUriParameterSupported() {
        return requestUriParameterSupported;
    }

    public void setRequestUriParameterSupported(Boolean requestUriParameterSupported) {
        this.requestUriParameterSupported = requestUriParameterSupported;
    }

    public List<String> getCodeChallengeMethodsSupported() {
        return codeChallengeMethodsSupported;
    }

    public void setCodeChallengeMethodsSupported(List<String> codeChallengeMethodsSupported) {
        this.codeChallengeMethodsSupported = codeChallengeMethodsSupported;
    }

    public Boolean getTlsClientCertificateBoundAccessTokens() {
        return tlsClientCertificateBoundAccessTokens;
    }

    public void setTlsClientCertificateBoundAccessTokens(Boolean tlsClientCertificateBoundAccessTokens) {
        this.tlsClientCertificateBoundAccessTokens = tlsClientCertificateBoundAccessTokens;
    }

    @Override
    public String toString() {
        return "OIDCConfiguration{" +
                "issuer='" + issuer + '\'' +
                ", authorizationEndpoint='" + authorizationEndpoint + '\'' +
                ", tokenEndpoint='" + tokenEndpoint + '\'' +
                ", tokenIntrospectionEndpoint='" + tokenIntrospectionEndpoint + '\'' +
                ", userinfoEndpoint='" + userinfoEndpoint + '\'' +
                ", logoutEndpoint='" + logoutEndpoint + '\'' +
                ", jwksUri='" + jwksUri + '\'' +
                ", checkSessionIframe='" + checkSessionIframe + '\'' +
                ", grantTypesSupported=" + grantTypesSupported +
                ", responseTypesSupported=" + responseTypesSupported +
                ", subjectTypesSupported=" + subjectTypesSupported +
                ", idTokenSigningAlgValuesSupported=" + idTokenSigningAlgValuesSupported +
                ", userInfoSigningAlgValuesSupported=" + userInfoSigningAlgValuesSupported +
                ", requestObjectSigningAlgValuesSupported=" + requestObjectSigningAlgValuesSupported +
                ", responseModesSupported=" + responseModesSupported +
                ", registrationEndpoint='" + registrationEndpoint + '\'' +
                ", tokenEndpointAuthMethodsSupported=" + tokenEndpointAuthMethodsSupported +
                ", tokenEndpointAuthSigningAlgValuesSupported=" + tokenEndpointAuthSigningAlgValuesSupported +
                ", claimsSupported=" + claimsSupported +
                ", claimTypesSupported=" + claimTypesSupported +
                ", claimsParameterSupported=" + claimsParameterSupported +
                ", scopesSupported=" + scopesSupported +
                ", requestParameterSupported=" + requestParameterSupported +
                ", requestUriParameterSupported=" + requestUriParameterSupported +
                ", codeChallengeMethodsSupported=" + codeChallengeMethodsSupported +
                ", tlsClientCertificateBoundAccessTokens=" + tlsClientCertificateBoundAccessTokens +
                '}';
    }
}
