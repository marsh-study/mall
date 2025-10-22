package com.mall.backend.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录响应VO")
public class LoginVO {

    @Schema(description = "令牌类型")
    private String tokenType;

    @Schema(description = "访问令牌")
    private String accessToken;

    @Schema(description = "过期时间(秒)")
    private String expiresIn;

    @Schema(description = "刷新令牌")
    private String refreshToken;

    public LoginVO() {
        this.tokenType = "Bearer";
    }
}