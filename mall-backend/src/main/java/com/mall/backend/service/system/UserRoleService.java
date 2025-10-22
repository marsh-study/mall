package com.mall.backend.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.backend.model.entity.SysRole;
import com.mall.backend.model.entity.SysUserRole;

import java.util.List;
import java.util.Map;

public interface UserRoleService extends IService<SysUserRole> {

    Map<Long, List<SysRole>> getRolesByUserIds(List<Long> userIds);
}
