package com.mall.backend.controller;

import com.mall.backend.annotation.SystemLog;
import com.mall.backend.enums.ResultCode;
import com.mall.backend.exception.BusinessException;
import com.mall.backend.model.dto.LoginRequest;
import com.mall.backend.model.vo.CaptchaVO;
import com.mall.backend.model.vo.LoginVO;
import com.mall.backend.service.AuthService;
import com.mall.backend.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.ExplainStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @GetMapping("/captcha")
    public Result<CaptchaVO> getCaptcha(){
        log.info("获取验证码");
        CaptchaVO captchaVo = authService.getCaptcha();
        return Result.success(captchaVo);
    }

    @PostMapping("/login")
    @SystemLog(module = "认证", operation = "用户登录")
    public Result<LoginVO> login(@RequestBody LoginRequest loginRequest){
        if (!authService.validateCaptcha(loginRequest)){
            return Result.fail(ResultCode.CAPTCHA_ERROR);
        }

        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            LoginVO loginVO = authService.login(authenticate);
            return Result.success(loginVO);
        } catch (DisabledException e) {
            return Result.fail(ResultCode.A0232);
        } catch (BadCredentialsException e) {
            return Result.fail(ResultCode.USER_PASSWORD_ERROR);
        } catch (AuthenticationException e) {
            return Result.fail(ResultCode.AUTHENTICATION_ERROR.getCode(), ResultCode.AUTHENTICATION_ERROR.getMsg() + " " + e.getMessage());
        }
    }

    // 刷新 Token 接口
    @PostMapping("/refresh")
    public Result<LoginVO> refreshToken(@RequestBody Map<String, String> tokenRequest) {
        log.info("刷新令牌请求: {}", tokenRequest);
        String bearerToken = tokenRequest.get("refreshToken");
        
        // 检查 refreshToken 是否为空
        if (bearerToken == null || bearerToken.trim().isEmpty()) {
            return Result.fail("刷新令牌不能为空");
        }

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String refreshToken=bearerToken.substring(7); // 去掉 "Bearer " 前缀
            return authService.refreshToken(refreshToken);
        }else {
            return Result.fail("令牌格式有误");
        }
    }

    @DeleteMapping ("/logout")
    public Result logout(){
        return Result.success();
    }

}
