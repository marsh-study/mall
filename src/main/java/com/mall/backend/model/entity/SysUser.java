package com.mall.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUser{
  @TableId(value = "id",type = IdType.AUTO)
  private Long id;
  //用户名
  private String username;
  //昵称
  private String nickname;
  //性别
  private Integer gender;
  //密码
  @JsonIgnore
  private String password;
  private String avatar;
  private String mobile;
  private Integer status;
  private String email;
  private Integer deleted;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Timestamp createTime;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Timestamp updateTime;
  private Long createBy;
  private Long updateBy;

  @TableField(exist = false)
  private List<SysRole> roles;


}
