package com.mall.backend.util;

public class VerificationCodeUtil {

    public static String getVerificationCode(Integer  length) {
        StringBuilder verificationCode = new StringBuilder();
        for (int i = 0; i < length; i++) {
            verificationCode.append((int)(Math.random() * 10));
        }
        return verificationCode.toString();
    }
}
