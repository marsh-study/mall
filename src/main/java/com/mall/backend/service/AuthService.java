package com.mall.backend.service;

import cn.hutool.captcha.AbstractCaptcha;
import com.mall.backend.model.dto.LoginRequest;
import com.mall.backend.model.vo.CaptchaVO;
import com.mall.backend.model.vo.LoginVO;
import com.mall.backend.service.component.BlacklistService;
import com.mall.backend.service.system.impl.UserDetailsServiceImpl;
import com.mall.backend.util.JwtUtil;
import com.mall.backend.util.Result;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;


import java.security.SecureClassLoader;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthService {
    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${captcha.expire-in-seconds}")
    private int captchaExpireInSeconds;

    // 新增登录版本号前缀常量
    private static final String LOGIN_VERSION_PREFIX = "user:login_version:";

    /**
     * 获取验证码
     */
    public CaptchaVO getCaptcha(){
        AbstractCaptcha generate = captchaService.generate();
        //将验证码存储到redis
        String captchaCode = generate.getImageBase64Data();
        String captchaId = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("captcha:"+captchaId, generate.getCode(), captchaExpireInSeconds, TimeUnit.SECONDS);
        //返回验证码
        return new CaptchaVO(captchaId, captchaCode);
    }

    //验证验证码
    public boolean validateCaptcha(LoginRequest loginRequest) {
        String cacheKey = "captcha:" + loginRequest.getCaptchaId();
        String cacheCode = redisTemplate.opsForValue().get(cacheKey);

        if (cacheCode == null || !cacheCode.equalsIgnoreCase(loginRequest.getCaptchaCode())) {
            return false;
        }

        redisTemplate.delete(cacheKey);
        return true;
    }

    /**
     * 登录
     */
    public LoginVO login(Authentication authenticate) {
        String username = authenticate.getName();
        Collection<? extends GrantedAuthority> authorities = authenticate.getAuthorities();
//        log.info("用户 {} 登录成功，权限为 {}", username, authorities);

        // 使之前所有token失效（通过更新版本号）
        redisTemplate.opsForValue().increment(LOGIN_VERSION_PREFIX + username, 1);

        // 生成新token时携带当前版本号
        Long currentVersion = Long.parseLong(redisTemplate.opsForValue().get(LOGIN_VERSION_PREFIX + username));
        // 设置版本号有效期（例如30天自动过期）
        redisTemplate.expire(LOGIN_VERSION_PREFIX + username, 30, TimeUnit.DAYS);

        LoginVO loginVO = new LoginVO();
        String accessToken = jwtUtil.generateAccessToken(username, authorities, currentVersion);
        String refreshToken = jwtUtil.generateRefreshToken(username);
        loginVO.setAccessToken(accessToken);
        loginVO.setExpiresIn(jwtUtil.getAccessExpireSeconds().toString());
        loginVO.setRefreshToken(refreshToken);
        return loginVO;
    }


    public Result<LoginVO> refreshToken(String refreshToken) {

        // 验证refresh token类型和有效性
        if (!jwtUtil.isRefreshTokenValid(refreshToken)) {
            return Result.fail("无效的刷新令牌或令牌已过期");
        }
        
        // 检查黑名单
        String jti = jwtUtil.getJtiFromToken(refreshToken);
        if (blacklistService.isInBlacklist(jti)) {
            return Result.fail("令牌已在黑名单中，无法使用");
        }

        // 获取用户信息
        String username = jwtUtil.getClaim(refreshToken, Claims::getSubject);
        if (username == null || username.isEmpty()) {
            return Result.fail("无法从令牌中获取用户信息");
        }

        // 使旧refresh token失效
        try {
            Date expiration = jwtUtil.getClaim(refreshToken, Claims::getExpiration);
            if (expiration != null) {
                long ttl = expiration.getTime() - System.currentTimeMillis();
                blacklistService.addToBlacklist(jti, ttl);  // 使用黑名单服务
            }
        } catch (Exception e) {
            // 即使黑名单添加失败，也不应该影响刷新令牌流程
            log.warn("添加令牌到黑名单失败: {}", e.getMessage());
        }

        // 获取用户权限
        Collection<? extends GrantedAuthority> authorities = userDetailsService.loadUserByUsername(username).getAuthorities();

        // 使之前所有token失效（通过更新版本号）
        redisTemplate.opsForValue().increment(LOGIN_VERSION_PREFIX + username, 1);

        // 生成新token时携带当前版本号
        Long currentVersion = Long.parseLong(redisTemplate.opsForValue().get(LOGIN_VERSION_PREFIX + username));

        // 生成新token
        String newAccessToken = jwtUtil.generateAccessToken(username, authorities,currentVersion);

        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken(newAccessToken);
        loginVO.setExpiresIn(jwtUtil.getAccessExpireSeconds().toString());
        loginVO.setRefreshToken(jwtUtil.generateRefreshToken(username)); // 生成新的刷新令牌
        return Result.success(loginVO);
    }
}
