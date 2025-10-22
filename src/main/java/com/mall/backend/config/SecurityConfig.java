package com.mall.backend.config;

import com.mall.backend.filter.JwtAuthenticationFilter;
import com.mall.backend.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 1. 密码加密器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. 安全过滤链配置
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 前后端分离项目通常关闭 CSRF

                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 启用 CORS

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 无状态
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/register", "/auth/refresh","/auth/captcha","/auth/logout").permitAll()
                        .requestMatchers(HttpMethod.GET, "/files/*").permitAll()
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/orders", "/orders/**").permitAll() // 放行下单接口
                        .requestMatchers( "/actuator/health").permitAll()
                        .requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )

                // ❌ 移除 formLogin 和 logout：我们用 JWT，不依赖 Spring Security 的 Session 登录机制
                // 所有认证由 JWT + 自定义 Filter 处理

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(unauthorizedHandler())   // 401 未登录
                        .accessDeniedHandler(accessDeniedHandler())       // 403 权限不足
                )

                // ✅ 在 UsernamePasswordAuthenticationFilter 之前加入 JWT 过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 3. CORS 配置（生产环境建议更严格）
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*")); // 允许所有域名（开发用），生产建议指定如 "http://localhost:5173"
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // 允许携带 Cookie（如果不用 Cookie 可设为 false）
        config.setExposedHeaders(List.of("Authorization")); // 暴露给前端的 header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // 4. 智能判断
    @Bean
    public org.springframework.security.web.AuthenticationEntryPoint unauthorizedHandler() {
        return (request, response, authException) -> {
            response.setStatus(UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            Result<Object> result = Result.fail("401", "请先登录");
            response.getWriter().write("{\"code\":\"" + result.getCode() + "\",\"msg\":\"" + result.getMsg() + "\",\"data\":null}");
        };
    }

    // 5. 权限不足处理器（403）
    @Bean
    public org.springframework.security.web.access.AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(FORBIDDEN.value());
            response.setContentType("application/json;charset=UTF-8");
            Result<Object> result = Result.fail("403", "权限不足");
            response.getWriter().write("{\"code\":\"" + result.getCode() + "\",\"msg\":\"" + result.getMsg() + "\",\"data\":null}");
        };
    }

    // ✅ 提示：登录和登出接口由你自己在 Controller 中实现
    // 例如：
    // - POST /auth/login  -> 返回 { accessToken, refreshToken }
    // - POST /auth/refresh -> 使用 refresh token 换新 access token
    // - POST /auth/logout  -> 可选：将 refresh token 加入黑名单
}