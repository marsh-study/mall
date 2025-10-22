package com.mall.backend.util;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        // 生成 HS256 算法要求的 256 位（32 字节）密钥
        byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
        // 编码为 Base64 字符串（便于配置存储）
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("生成的安全密钥（Base64 编码）：" + base64Key);
    }
}
