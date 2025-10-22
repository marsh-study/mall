package com.mall.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    // 配置参数
    @Value("${jwt.expire-in-seconds}")
    private Long accessExpireSeconds;  // Access Token有效期（秒）

    @Value("${jwt.refresh-expire-in-seconds}")
    private Long refreshExpireSeconds;

    @Value("${jwt.secret}")
    private String secret;  // JWT签名密钥

    @Autowired
    private StringRedisTemplate redisTemplate;  // Redis操作模板

    private SecretKey secretKey;  // 加密密钥对象

    // 初始化密钥
    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // ================== Token 生成 ==================
    /**
     * 生成Access Token
     * @param username 用户名
     * @param authorities 权限列表
     * @param version 登录版本号
     */
    public String generateAccessToken(String username, Collection<? extends GrantedAuthority> authorities, Long version) {
        return buildToken(username, "access", authorities, version, accessExpireSeconds);
    }

    /**
     * 生成Refresh Token
     * @param username 用户名
     */
    public String generateRefreshToken(String username) {
        return buildToken(username, "refresh", null, null, refreshExpireSeconds);
    }

    // ================== Token 验证 ==================
    /**
     * 验证Access Token有效性
     * @param token Access Token
     */
    public boolean isAccessTokenValid(String token) {
        return validateTokenType(token, "access");
    }

    /**
     * 验证Refresh Token有效性
     * @param token Refresh Token
     */
    public boolean isRefreshTokenValid(String token) {
        return validateTokenType(token, "refresh");
    }

    // ================== 声明(Claims) 操作 ==================
    /**
     * 获取Token中的所有声明（不抛异常）
     */
    public Claims getAllClaimsOrNull(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从Token中获取指定声明
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsOrNull(token);
        return claims != null ? claimsResolver.apply(claims) : null;
    }

    // ================== 辅助方法 ==================
    /**
     * 统一Token构建方法
     */
    private String buildToken(String username, String type,
                              Collection<? extends GrantedAuthority> authorities,
                              Long version, Long expireSeconds) {
        JwtBuilder builder = Jwts.builder()
                .subject(username)
                .id(UUID.randomUUID().toString())
                .claim("type", type)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expireSeconds * 1000))
                .signWith(secretKey);

        if (authorities != null) {
            builder.claim("authorities", authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
        }
        if (version != null) {
            builder.claim("version", version);
        }
        return builder.compact();
    }

    /**
     * 验证特定类型Token的有效性
     */
    private boolean validateTokenType(String token, String expectedType) {
        Claims claims = getAllClaimsOrNull(token);
        return claims != null
                && expectedType.equals(claims.get("type"))
                && !isExpired(claims.getExpiration());
    }

    /**
     * 验证Token版本有效性
     */
    public boolean validateTokenVersion(String username, Long tokenVersion) {
        try {
            String storedVersion = redisTemplate.opsForValue().get("user:login_version:" + username);
            return storedVersion != null
                    && tokenVersion != null
                    && tokenVersion.equals(Long.parseLong(storedVersion));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断过期时间是否有效
     */
    public boolean isExpired(Date expiration) {
        return expiration.before(new Date());
    }

    // ================== 声明(Claims) 操作 ==================
    /**
     * 获取JWT唯一标识(JTI)
     */
    public String getJtiFromToken(String token) {
        return getClaim(token, Claims::getId);
    }

    /**
     * 获取Token中的版本号
     */
    public Long getVersionFromToken(String token) {
        return getClaim(token, claims -> claims.get("version", Long.class));
    }

    // ================== Getter 方法 ==================
    public Long getAccessExpireSeconds() {
        return accessExpireSeconds;
    }

    public Long getRefreshExpireSeconds() {
        return refreshExpireSeconds;
    }
}