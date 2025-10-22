package com.mall.backend.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserVO {
    private Long userId;
    private String username;
    private String nickname;
    private String avatar;
    //用户角色编码集合
    private List<String> roles;
    //用户权限标识集合
    private List<String> perms;
    private Integer gender;
    private String mobile;
    private String email;
    private Integer status;
}
