package com.mall.backend.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.backend.common.model.OptionType;
import com.mall.backend.model.entity.SysMenu;
import com.mall.backend.model.entity.SysRole;
import com.mall.backend.model.form.RoleForm;
import com.mall.backend.model.query.RoleQuery;
import com.mall.backend.model.vo.RolePageVO;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;

import java.util.List;

public interface RoleService extends IService<SysRole> {

    List<SysMenu> getPermsByRoleIds(List<Long> roleIds);

    List<OptionType<Long>> getRoleOptions(RoleQuery roleQuery);

    Result<PageResult<RolePageVO>> getRolePage(RoleQuery roleQuery);

    Result addRole(RoleForm roleForm);

    Result<RoleForm> getRoleForm(Long id);

    Result updateRole(Long id, RoleForm roleForm);


    Result deleteRoles(List<Long> idList);

    Result<List<Long>> getRoleMenuIds(Long roleId);

    Result updateRoleMenuIds(Long roleId, List<Long> menuIds);
}
