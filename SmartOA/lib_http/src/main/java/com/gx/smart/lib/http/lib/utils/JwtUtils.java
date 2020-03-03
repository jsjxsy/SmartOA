package com.gx.smart.lib.http.lib.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.Map;

/**
 * @Author niling on 2019/6/19
 */
public class JwtUtils {
    private static final String RSA = "RSA";

    public static Algorithm getAlgorithmRSA256(String publicKey, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(decodeBase64ToBytes(publicKey)));
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decodeBase64ToBytes(privateKey)));
        return Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
    }

    public static String createToken(String issuer,
                                     String keyId,
                                     String clientId,
                                     String subject,
                                     Algorithm algorithm,
                                     int expires,
                                     Map<String, Object> headers,
                                     Map<String, String> payload) {
        JWTCreator.Builder builder = JWT.create();
        builder.withIssuer(issuer);
        builder.withKeyId(keyId);
        builder.withSubject(subject);
        builder.withAudience(clientId);

        Calendar calendar = Calendar.getInstance();
        builder.withIssuedAt(calendar.getTime());
        calendar.add(Calendar.SECOND, expires);
        builder.withExpiresAt(calendar.getTime());

        if (null != headers && headers.size() > 0) {
            builder.withHeader(headers);
        }
        if (null != payload && payload.size() > 0) {
            for (Map.Entry<String, String> entry : payload.entrySet()) {
                builder.withClaim(entry.getKey(), entry.getValue());
            }
        }
        return builder.sign(algorithm);
    }

//    public static String decodeBase64(String src) {
//        return new String(Base64.getDecoder().decode(src));
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static byte[] decodeBase64ToBytes(String src) {
        return Base64.getDecoder().decode(src);
    }

//    public static String encodeBase64(byte[] bytes) {
//        return Base64.getEncoder().encodeToString(bytes);
//    }


}
