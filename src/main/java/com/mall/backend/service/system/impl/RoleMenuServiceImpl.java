package com.mall.backend.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.backend.mapper.system.RoleMenuMapper;
import com.mall.backend.model.entity.SysRoleMenu;
import com.mall.backend.service.system.RoleMenuService;
import org.springframework.stereotype.Service;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, SysRoleMenu> implements RoleMenuService {
}
