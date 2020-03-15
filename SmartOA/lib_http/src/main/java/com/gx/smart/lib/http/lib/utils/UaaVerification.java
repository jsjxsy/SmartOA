package com.gx.smart.lib.http.lib.utils;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.gx.smart.lib.http.lib.constants.AuthConstants;
import com.gx.smart.lib.http.lib.model.JwtHolder;
import com.gx.smart.lib.http.lib.model.OIDCConfiguration;
import com.gx.smart.lib.http.lib.model.VerificationKey;
import com.gx.smart.lib.http.lib.model.VerificationKeys;

import org.jose4j.jwk.RsaJsonWebKey;

import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author niling on 2019/7/8
 */
public class UaaVerification {

    public boolean issuerFilter = false;
    public boolean clientFilter = false;
    private ConcurrentHashMap<String, List<String>> accepts = new ConcurrentHashMap<>();
    private KeyProvider keyProvider;


    public UaaVerification() {
        initDefKeyProvider();
    }
    public UaaVerification(KeyProvider keyProvider) {
        this.keyProvider = keyProvider;
        if (null == this.keyProvider) {
            initDefKeyProvider();
        }
    }

    public static void main(String[] args) throws Exception {
        String token = "eyJraWQiOiIzNzg5ZWI5NCIsInR5cCI6IkpXVCIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJuaWxpbmciLCJhdWQiOiI2OGNlMDczOCIsInVzcCI6ImRmYTI5YThlIiwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMTozMTc3MS9hdXRoL3N5c2FwaSIsImV4cCI6MTU2NDM3MDkzNCwiaWF0IjoxNTY0MzY5MTM0fQ.b2XCye8lKEL9_jKQOdOSQ5yjRvxLhIW7_-d8uaw8r1dx1rm4QOD2MrzPh3mz_WiYFwq60tHFzlF-wRi5LGxyNstmr8bN1CaaytRbDtP_6BxpuUbvB_tSTelIBTY4eKs9lOICSYfNsJ2ZId43Cpox5DhUXZDkgAreylpDZfgs_qcUj1y9zxS6moPZAJZkxKdrXEwvQsuSizijtZW1cIE-evTn6M0REWAa9Jxypj6aHfvy-R5ANw7XnEb6vY71qRHMlo4Ym0lh4WkC0gizvruYHi-rXuYpw2Tn7eni-u27guAuKKFvb9lEkM03RAmUBpljM0CJWayg3D7ju0eSw2HsLw";
        UaaVerification verification = new UaaVerification();
        verification.clientFilter = true;
        verification.issuerFilter = true;
        verification.addFilter("http://127.0.0.1:31771/auth/sysapi", "68ce0738");

        boolean flag = verification.OIDCVerify(AuthConstants.TOKEN_PREFIX + token);
        System.out.println(flag);
    }

    public void initDefKeyProvider() {
        this.keyProvider = new RemoteKeyProvider();
    }

    public KeyProvider getKeyProvider() {
        return keyProvider;
    }

    public void setKeyProvider(KeyProvider keyProvider) {
        if (null != keyProvider) {
            this.keyProvider = keyProvider;
        }
    }

    public void addFilter(String issuer, String... clientIds) {
        if (null == issuer) {
            return;
        }
        synchronized (this) {
            List<String> clientId = null;
            if (null != clientIds && clientIds.length > 0) {
                clientId = Arrays.asList(clientIds);
            } else {
                clientId = Collections.EMPTY_LIST;
            }
            accepts.put(issuer, clientId);
        }

    }

    private boolean filter(JwtHolder holder) {
        if (!issuerFilter) {
            return true;
        }
        List<String> clientIds = accepts.get(holder.getIssuer());
        if (null == clientIds) {
            return false;
        }
        if (clientFilter) {
            boolean flag = false;
            if (clientIds.size() > 0) {
                for (String clientId : clientIds) {
                    String audience = holder.getAudience();
                    if (null != audience && audience.contains(clientId)) {
                        flag = true;
                        break;
                    }
                }
            }
            return flag;
        }
        return true;
    }

    public JwtHolder parseHolderByTokens(String tokens) {
        String token = null;
        if (null != tokens &&
                tokens.length() > AuthConstants.TOKEN_PREFIX.length() &&
                AuthConstants.TOKEN_PREFIX.equalsIgnoreCase(tokens.substring(0, AuthConstants.TOKEN_PREFIX.length()))) {
            token = tokens.substring(AuthConstants.TOKEN_PREFIX.length());
        }
        return parseHolderByToken(token);
    }

    public JwtHolder parseHolderByToken(String token) {
        return AuthUtils.parseJwtHolder(token);
    }

    public boolean OIDCVerify(JwtHolder holder) throws Exception {
        //TODO 优化 缓存 提高效率
        boolean flag = false;
        if (holder.status) {
            if (null == holder.getIssuer() || null == holder.getAudience() || null == holder.getSubject()) {
                return flag;
            }
            if (!filter(holder)) {
                return flag;
            }
            Algorithm algorithm = getAlg(holder);
            if (null == algorithm) {
                return flag;
            }
            Verification verification = JWT.require(algorithm);
            verification.withIssuer(holder.getIssuer());
            verification.withAudience(holder.getAudience());
            verification.withSubject(holder.getSubject());

            JWTVerifier verifier = verification.build();
            DecodedJWT result = verifier.verify(holder.getToken());
            Date expiresAt = result.getExpiresAt();
            if (System.currentTimeMillis() <= expiresAt.getTime()) {
                flag = true;
            }
        }

        return flag;
    }

    public boolean OIDCVerify(String tokens) throws Exception {
        return OIDCVerifyTokens(tokens);
    }

    public boolean OIDCVerifyToken(String token) throws Exception {
        return OIDCVerify(parseHolderByToken(token));
    }

    public boolean OIDCVerifyTokens(String tokens) throws Exception {
        return OIDCVerify(parseHolderByTokens(tokens));
    }


    private Algorithm getAlg(JwtHolder holder) throws Exception {
        if (null == holder.getKeyId() || null == holder.getKeyType() || null == holder.getAlgorithm()) {
            return null;
        }
        String issuer = holder.getIssuer();
        String keyId = holder.getKeyId();

        VerificationKey verificationKey = keyProvider.getKey(issuer, keyId);
        if (null == verificationKey) {
            return null;
        }
        //TODO 判断 key的类型等参数

        RsaJsonWebKey jsonWebKey = new RsaJsonWebKey(verificationKey.parseToParams());
        RSAPublicKey rsaPublicKey = jsonWebKey.getRsaPublicKey();
        return Algorithm.RSA256(rsaPublicKey, null);
    }


    public interface KeyProvider {
        VerificationKey getKey(String issuer, String keyId) throws Exception;

        void clearIssuers();
    }

    public static class RemoteKeyProvider implements KeyProvider {
        private static final long CLEAR_TIME = 1000 * 60;
        private final ConcurrentHashMap<String, Map<String, VerificationKey>> issuers = new ConcurrentHashMap<>();
        private long lastClear = 0;

        @Override
        public VerificationKey getKey(String issuer, String keyId) throws Exception {
            if (System.currentTimeMillis() > (lastClear + CLEAR_TIME)) {
                clearIssuers();
            }
            Map<String, VerificationKey> keysMap = issuers.get(issuer);
            if (null == keysMap) {
                synchronized (this) {
                    keysMap = issuers.get(issuer);
                    if (null == keysMap) {
                        VerificationKeys keys = null;
                        String jwksUri = getOIDCConfiguration(issuer).getJwksUri();
                        if (null == jwksUri) {
                            return null;
                        }
                        keys = getKeys(jwksUri);
                        if (null == keys) {
                            return null;
                        }

                        keysMap = new HashMap<>();
                        if (null != keys.getKeys()) {
                            for (VerificationKey key : keys.getKeys()) {
                                keysMap.put(key.getKid(), key);
                            }
                        }
                        issuers.put(issuer, keysMap);
                    }
                }
            }

            return keysMap.get(keyId);
        }

        @Override
        public void clearIssuers() {
            synchronized (this) {
                issuers.clear();
                lastClear = System.currentTimeMillis();
            }
        }

        private OIDCConfiguration getOIDCConfiguration(String issuer) throws Exception {
            String json = getHttpJson(issuer + AuthConstants.OIDC_CONFIG);
            OIDCConfiguration configuration = OIDCConfiguration.parseFromJson(json);
            return configuration;
        }

        private VerificationKeys getKeys(String url) throws Exception {
            String json = getHttpJson(url);
            return JSON.parseObject(json, VerificationKeys.class);
        }

        private String getHttpJson(String url) throws Exception {
            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(new Request.Builder().url(url).build());
            Response response = call.execute();
            String json = response.body().string();
            return json;
        }
    }

}
