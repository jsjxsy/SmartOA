package com.gx.smart.smartoa.data.network.api.lib.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author niling on 2019/6/19
 */
public class VerificationKey {

    /**
     * 密钥ID
     */
    private String kid;
    /**
     * 公钥算法类型
     * RSA
     * EC
     */
    private String kty;
    /**
     * 公钥算法(含位数)
     * RS256
     */
    private String alg;
    /**
     * 公钥用途
     * sig 签名
     * enc 加密
     */
    private String use;
    /**
     * 公钥
     */
    private String n;
    /**
     * AQAB
     */
    private String e;

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public String getKty() {
        return kty;
    }

    public void setKty(String kty) {
        this.kty = kty;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public Map<String, Object> parseToParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("kid",kid);
        params.put("kty",kty);
        params.put("alg",alg);
        params.put("use",use);
        params.put("n",n);
        params.put("e",e);
        return params;
    }

    @Override
    public String toString() {
        return "VerificationKey{" +
                "kid='" + kid + '\'' +
                ", kty='" + kty + '\'' +
                ", alg='" + alg + '\'' +
                ", use='" + use + '\'' +
                ", n='" + n + '\'' +
                ", e='" + e + '\'' +
                '}';
    }
}
