package com.mall.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mall.backend.common.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_menu")
public class SysMenu extends BaseEntity {

  @TableId(type = IdType.AUTO)
  private Long id;
  private Long parentId;
  private Integer type;
  private String name;
  private String path;
  private String component;
  private String perm;
  private String icon;
  private Integer sort;
  private Integer visible;
  private String redirect;
  private String treePath;
  private Integer alwaysShow;
  private Integer keepAlive;

  @TableField(exist = false)
  private List<SysRole> roles;


}
