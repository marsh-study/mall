package com.mall.backend.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.backend.mapper.system.UserRoleMapper;
import com.mall.backend.model.entity.SysRole;
import com.mall.backend.model.entity.SysUserRole;
import com.mall.backend.service.system.RoleService;
import com.mall.backend.service.system.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, SysUserRole> implements UserRoleService {

    @Autowired
    private RoleService roleService;

    @Override
    public Map<Long, List<SysRole>> getRolesByUserIds(List<Long> userIds) {
        // 空集合处理
        if (userIds == null || userIds.isEmpty()) {
            return Map.of();
        }

        // 批量查询所有用户的角色关联
        List<SysUserRole> allUserRoles = this.list(
        new QueryWrapper<SysUserRole>().in("user_id", userIds)
        );

        // 提取所有角色ID并去重
        List<Long> roleIds = allUserRoles.stream()
            .map(SysUserRole::getRoleId)
            .distinct()
            .collect(Collectors.toList());

        // 批量查询所有角色
        Map<Long, SysRole> roleMap = roleService.listByIds(roleIds).stream()
            .collect(Collectors.toMap(SysRole::getId, Function.identity()));

        // 构建用户-角色映射
        Map<Long, List<SysRole>> result = allUserRoles.stream()
            .collect(Collectors.groupingBy(
                SysUserRole::getUserId,
                Collectors.mapping(
                    ur -> roleMap.get(ur.getRoleId()),
                    Collectors.toList()
                )
            ));
        return result;
            }
}
