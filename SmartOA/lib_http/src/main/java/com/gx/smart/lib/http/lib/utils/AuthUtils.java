package com.gx.smart.lib.http.lib.utils;

import android.util.Base64;

import com.alibaba.fastjson.JSON;
import com.gx.smart.lib.http.lib.constants.AuthConstants;
import com.gx.smart.lib.http.lib.model.JwtHolder;

import java.util.Map;

/**
 * @Author niling on 2019/7/9
 */
public class AuthUtils {

    public static String decodeBase64(String src) {
        return new String(Base64.decode(src, Base64.DEFAULT));
    }

    public static byte[] decodeBase64ToBytes(String src) {
        return Base64.decode(src, Base64.DEFAULT);
    }

    public static String encodeBase64(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static JwtHolder parseJwtHolder(String token) {
        JwtHolder holder = new JwtHolder();
        holder.setToken(token);
        holder.status = false;
        if (null == token) {
            return holder;
        }
        String[] ss = token.split(AuthConstants.SPLIT);
        if (null == ss || ss.length != 3) {
            return holder;
        }
        try {
            holder.setHeadersJson(decodeBase64(ss[0]));
            holder.setPayloadJson(decodeBase64(ss[1]));
            holder.setHeaders(JSON.parseObject(holder.getHeadersJson(), Map.class));
            holder.setPayload(JSON.parseObject(holder.getPayloadJson(), Map.class));

            holder.setIssuer(getString(holder.getPayload().get(AuthConstants.PAYLOAD_ISSUER)));
            holder.setAudience(getString(holder.getPayload().get(AuthConstants.PAYLOAD_AUDIENCE)));
            holder.setSubject(getString(holder.getPayload().get(AuthConstants.PAYLOAD_SUBJECT)));

            holder.setKeyId(getString(holder.getHeaders().get(AuthConstants.HEADER_KEY_ID)));
            holder.setKeyType(getString(holder.getHeaders().get(AuthConstants.HEADER_TYPE)));
            holder.setAlgorithm(getString(holder.getHeaders().get(AuthConstants.HEADER_ALGORITHM)));

            holder.status = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return holder;
    }

    public static boolean checkJwtHolder(JwtHolder jwtHolder) {
        if (null == jwtHolder) {
            return false;
        }
        if (isEmpty(jwtHolder.getIssuer())) {
            return false;
        }
        if (isEmpty(jwtHolder.getAudience())) {
            return false;
        }
        if (isEmpty(jwtHolder.getSubject())) {
            return false;
        }
        if (isEmpty(jwtHolder.getKeyId())) {
            return false;
        }
        if (isEmpty(jwtHolder.getKeyType())) {
            return false;
        }
        if (isEmpty(jwtHolder.getAlgorithm())) {
            return false;
        }

        return true;
    }

    private static boolean isEmpty(String src) {
        if (null != src && src.length() > 0) {
            return false;
        }
        return true;
    }

    private static String getString(Object obj) {
        if (null != obj) {
            String s = obj.toString();
            if (null != s && s.length() > 0) {
                return s;
            }
        }
        return null;
    }
}
