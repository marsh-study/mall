package com.mall.backend.filter;

import com.mall.backend.enums.ResultCode;
import com.mall.backend.util.JwtUtil;
import com.mall.backend.util.Result;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * JWT 认证过滤器
 * 在 UsernamePasswordAuthenticationFilter 之前执行
 * 负责从 Authorization Header 中提取 JWT，验证并设置 SecurityContext
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtUtil jwtUtil;

    private final StringRedisTemplate redisTemplate;  // 必须注入RedisTemplate

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 修复逻辑：应该排除 /auth/ 路径下的请求，而不是只允许 /auth/refresh
        if(request.getServletPath().startsWith("/auth/")){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = extractToken(request);
            if (token != null) {
                // 统一使用claims进行校验
                Claims claims = jwtUtil.getAllClaimsOrNull(token);
                if (claims == null || !jwtUtil.isAccessTokenValid(token)) {
                    handleInvalidToken(response, ResultCode.A0230);
                    return;
                }

                // 黑名单检查
                String jti = claims.getId(); // 直接从claims获取jti
                if (StringUtils.hasText(jti) && redisTemplate.hasKey("blacklist::" + jti)) {
                    handleInvalidToken(response, ResultCode.A0232);
                    return;
                }

                // 类型和版本校验
                if (!jwtUtil.validateTokenVersion(claims.getSubject(),claims.get("version", Long.class))) {
                    handleInvalidToken(response, ResultCode.A0231);
                    return;
                }

                // 构建认证信息
                buildAuthentication(request, claims);
            }
        } catch (Exception e) {
            log.error("JWT 认证过程中发生错误: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }


    /**
     * 从请求头中提取 JWT Token
     * 格式：Bearer <token>
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // 去掉 "Bearer " 前缀
        }
        return null;
    }

    /**
     * 可选：跳过某些路径（如静态资源）
     * 如果返回 true，则该请求不经过此过滤器
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return "/auth/refresh".equals(path); // 对该路径不执行过滤器逻辑
    }

    private void buildAuthentication(HttpServletRequest request, Claims claims) {
        String username = claims.getSubject();
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            List<String> authorities = (List<String>) claims.get("authorities");
            Collection<GrantedAuthority> authorityList = authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorityList);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private void handleInvalidToken(HttpServletResponse response, ResultCode resultCode) {
        SecurityContextHolder.clearContext();
        Result result = resultCode != null ?
                Result.fail(resultCode.getCode(), resultCode.getMsg()) :
                Result.fail(resultCode.getMsg());
        Result.out(response, result);
    }

}