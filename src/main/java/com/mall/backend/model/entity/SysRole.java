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
@TableName("sys_role")
public class SysRole extends BaseEntity {

  @TableId(value = "id",type = IdType.AUTO)
  private Long id;
  private String name;
  private String code;
  private Integer sort;
  private Integer status;
  private Integer deleted;

  @TableField(exist = false)
  private List<SysMenu> sysMenuList;

}
