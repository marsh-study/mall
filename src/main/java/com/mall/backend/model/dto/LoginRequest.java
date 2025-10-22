package com.mall.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    /**
     * 验证码Code
     */
    private String captchaCode;
    /**
     * 验证码唯一标识(UUID)
     */
    private String captchaId;

}
