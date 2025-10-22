package com.mall.backend.controller;

import com.mall.backend.annotation.SystemLog;
import com.mall.backend.common.model.OptionType;
import com.mall.backend.exception.BusinessException;
import com.mall.backend.model.form.RoleForm;
import com.mall.backend.model.query.RoleQuery;
import com.mall.backend.model.vo.RolePageVO;
import com.mall.backend.service.system.RoleService;
import com.mall.backend.service.system.UserService;
import com.mall.backend.service.system.impl.UserServiceImpl;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserServiceImpl userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/options")
    @Schema(name = "获取角色下拉数据", description = "roleQuery")
    public Result<List<OptionType<Long>>> getRoleOptions(RoleQuery roleQuery){
        return Result.success(roleService.getRoleOptions(roleQuery));
    }

    /**
     * 获取角色分页数据
     * @param roleQuery
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/page")
    @Schema(name = "获取角色列表", description = "roleQuery")
    public Result<PageResult<RolePageVO>> getRolePage(RoleQuery roleQuery){
        return roleService.getRolePage(roleQuery);
    }

    /**
     * 添加角色
     * @param roleForm
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @SystemLog(module = "角色管理",operation = "添加角色")
    public Result addRole(@RequestBody RoleForm roleForm){
        return roleService.addRole(roleForm);
    }

    /**
     * 获取角色详情
     * @param id
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/form")
    public Result<RoleForm> getRoleForm(@PathVariable Long id){
        return roleService.getRoleForm(id);
    }

    /**
     * 修改角色
     * @param id
     * @param roleForm
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @SystemLog(module = "角色管理",operation = "修改角色")
    public Result updateRole(@PathVariable Long id, @RequestBody RoleForm roleForm){
        return roleService.updateRole(id, roleForm);
    }

    /**
     * 删除角色
     * @param ids
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{ids}")
    @SystemLog(module = "角色管理",operation = "删除角色")
    public Result deleteRole(@PathVariable String ids){
        if (ids == null || ids.isEmpty()) {
            return Result.fail("参数错误");
        }
        List<Long> idList= Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        if(idList.contains(userService.getCurrentUserId())){
            throw new BusinessException("不能删除自己");
        }
        return roleService.deleteRoles(idList);
    }

    /**
     * 获取角色的菜单ID集合
     * @param roleId
     * @return 菜单ID集合
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{roleId}/menuIds")
    public Result<List<Long>> getRoleMenuIds(@PathVariable Long roleId){
        return roleService.getRoleMenuIds(roleId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{roleId}/menus")
    public Result updateRoleMenuIds(@PathVariable Long roleId, @RequestBody List<Long> menuIds){
        if (menuIds == null || menuIds.isEmpty()) {
            return Result.fail("参数错误");
        }
        return roleService.updateRoleMenuIds(roleId, menuIds);
    }

}
