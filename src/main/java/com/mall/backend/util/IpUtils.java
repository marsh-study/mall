package com.mall.backend.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;

// IpUtils.java
public class IpUtils {
    public static String getClientIpAddress(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");

        if (xff != null && !xff.isEmpty() && !"unknown".equalsIgnoreCase(xff)) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
