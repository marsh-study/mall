package com.mall.backend.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPageVO {
    /**
     * 用户头像地址
     */
    @Schema(description = "用户头像地址")
    private String avatar;
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createTime;
    /**
     * 用户邮箱
     */
    @Schema(description = "用户邮箱")
    private String email;
    /**
     * 性别
     */
    @Schema(description = "性别")
    private String genderLabel;
    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long id;
    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String mobile;
    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String nickname;
    /**
     * 角色名称，多个使用英文逗号(,)分割
     */
    @Schema(description = "角色名称，多个使用英文逗号(,)分割")
    private String roleNames;
    /**
     * 用户状态(1:启用;0:禁用)
     */
    @Schema(description = "用户状态(1:启用;0:禁用)")
    private Integer status;
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;
}
