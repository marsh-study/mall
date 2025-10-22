package com.mall.backend.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.backend.model.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper extends BaseMapper<SysMenu> {
}
