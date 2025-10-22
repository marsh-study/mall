package com.mall.backend.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {
    /**
     * 用户头像
     */
    @Schema(description = "用户头像")
    private String avatar;
    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;
    /**
     * 性别
     */
    @Schema(description = "性别")
    private Integer gender;
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
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;
    /**
     * 角色ID集合
     */
    @Schema(description = "角色ID集合")
    private List<Long> roleIds;
    /**
     * 用户状态(1:正常;0:禁用)
     */
    @Schema(description = "用户状态(1:正常;0:禁用)")
    private Integer status;
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;
}
