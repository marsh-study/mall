package com.mall.backend.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.backend.model.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper extends BaseMapper<SysRole> {
}
