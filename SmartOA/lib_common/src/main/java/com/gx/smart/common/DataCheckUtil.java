package com.gx.smart.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataCheckUtil {

    //身份证号码验证
    public static boolean CheckIdCard(String IdNumber) {
        int dwLength = IdNumber.length();
//        if (dwLength == 15) return true;
        if (dwLength != 18) return false;
        int[] szWeight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
        char[] szCheck = {'1', '0', 'x', '9', '8', '7', '6', '5', '4', '3', '2'};

        int dwSum = 0;
        for (int i = 0; i < 17; ++i) {
            dwSum += (IdNumber.charAt(i) - 48) * szWeight[i];
        }
        int nIndex = dwSum % 11;
        char cCheck = szCheck[nIndex];
        if (cCheck == 'x' && IdNumber.charAt(17) == 'X') return true;
        return cCheck == IdNumber.charAt(17);
    }

    //手机号验证
    public static boolean isMobile(String phoneNumber) {
        Pattern pattern = Pattern.compile("^1[0-9]{10}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }


}
