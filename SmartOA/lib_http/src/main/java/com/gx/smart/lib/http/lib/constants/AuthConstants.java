package com.gx.smart.lib.http.lib.constants;

/**
 * @Author niling on 2019/7/8
 */
public interface AuthConstants {

    String USER_SPACE_IN_TOKEN_KEY = "usp";

    String TOKEN_PREFIX = "Bearer ";
    String SPLIT = "\\.";
    String PAYLOAD_ISSUER = "iss";
    String PAYLOAD_AUDIENCE = "aud";
    String PAYLOAD_SUBJECT = "sub";
    String PAYLOAD_EXPIRES_AT = "exp";
    String PAYLOAD_ISSUED_AT = "iat";
    String HEADER_ALGORITHM = "alg";
    String HEADER_TYPE = "typ";
    String HEADER_KEY_ID = "kid";
    String OIDC_CONFIG = "/.well-known/openid-configuration";

    String OIDC_ISSUER = "issuer";
    String OIDC_AUTHORIZATION_ENDPOINT = "authorization_endpoint";
    String OIDC_TOKEN_ENDPOINT = "token_endpoint";
    String OIDC_USERINFO_ENDPOINT = "userinfo_endpoint";
    String OIDC_REVOCATION_ENDPOINT = "revocation_endpoint";
    String OIDC_JWKS_URI = "jwks_uri";
    String OIDC_RESPONSE_TYPES_SUPPORTED = "response_types_supported";
    String OIDC_SUBJECT_TYPES_SUPPORED = "subject_types_supported";
    String OIDC_ID_TOKEN_SIGNING_ALG_VALUES_SUPPORTED = "id_token_signing_alg_values_supported";
    String OIDC_SCOPES_SUPPORED = "scopes_supported";
    String OIDC_TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED = "token_endpoint_auth_methods_supported";
    String OIDC_CLAIMS_SUPPORTED = "claims_supported";
    String OIDC_CODE_CHALLENGE_METHODS_SUPPORTED = "code_challenge_methods_supported";
}
