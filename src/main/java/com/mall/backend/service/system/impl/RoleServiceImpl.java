package com.mall.backend.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.backend.exception.BusinessException;
import com.mall.backend.mapper.system.RoleMapper;
import com.mall.backend.common.model.OptionType;
import com.mall.backend.model.entity.SysMenu;
import com.mall.backend.model.entity.SysRole;
import com.mall.backend.model.entity.SysRoleMenu;
import com.mall.backend.model.form.RoleForm;
import com.mall.backend.model.query.RoleQuery;
import com.mall.backend.model.vo.RolePageVO;
import com.mall.backend.service.system.MenuService;
import com.mall.backend.service.system.RoleMenuService;
import com.mall.backend.service.system.RoleService;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, SysRole> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private MenuService menuService;

    /**
     * 根据角色ID获取菜单权限
     * @param roleIds 角色ID列表
     * @return 菜单权限列表
     */
    @Override
    public List<SysMenu> getPermsByRoleIds(List<Long> roleIds) {
        // 空值校验
        if (roleIds == null || roleIds.isEmpty()) {
            return List.of(); // 返回空列表而不是 null
        }
        List<SysRoleMenu> sysRoleMenus = roleMenuService.list(new QueryWrapper<SysRoleMenu>().in("role_id", roleIds));
        List<Long> menuIds = sysRoleMenus.stream().map(SysRoleMenu::getMenuId).distinct().collect(Collectors.toList());
        List<SysMenu> sysMenus = menuService.list(new QueryWrapper<SysMenu>().in("id", menuIds).orderByAsc("sort"));
        //根据菜单 ID 查询菜单详情
        if (menuIds.isEmpty()) {
            return List.of();
        }
        return sysMenus;
    }

    @Override
    public List<OptionType<Long>> getRoleOptions(RoleQuery roleQuery) {

        if(roleQuery.getKeywords()==null || roleQuery.getKeywords().isEmpty()){
            List<SysRole> list = this.list();
            return list.stream().map(sysRole -> new OptionType<>(sysRole.getId(), sysRole.getName())).collect(Collectors.toList());
        }else {
            Page<SysRole> page = new Page<>(roleQuery.getPageNum(), roleQuery.getPageSize());
            QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("name", roleQuery.getKeywords());
            Page<SysRole> rolePage = this.page(page, queryWrapper);
            List<SysRole> list = rolePage.getRecords();
            return list.stream().map(sysRole -> new OptionType<>(sysRole.getId(), sysRole.getName())).collect(Collectors.toList());
        }
    }

    @Override
    public Result<PageResult<RolePageVO>> getRolePage(RoleQuery roleQuery) {

        if(roleQuery==null){
            return Result.fail("参数错误");
        }

        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.like(roleQuery.getKeywords()!=null,"name", roleQuery.getKeywords())
                .orderByAsc("sort");
        Page<SysRole> page = new Page<>(roleQuery.getPageNum(), roleQuery.getPageSize());

        Page<SysRole> rolePage = this.page(page, queryWrapper);

        List<RolePageVO> rolePageVOList=rolePage.getRecords().stream().map(sysRole -> {
            RolePageVO rolePageVO = new RolePageVO();
            BeanUtils.copyProperties(sysRole,rolePageVO);
            return rolePageVO;
        }).collect(Collectors.toList());

        PageResult<RolePageVO> pageResult = new PageResult<>(rolePageVOList, rolePage.getTotal());
        return Result.success(pageResult);
    }

    @Override
    public Result addRole(RoleForm roleForm) {

        if(roleForm==null){
            return Result.fail("角色信息不能为空");
        }

        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(roleForm,sysRole);
        sysRole.setDeleted(0);
        sysRole.setCreateTime(new Date());
        sysRole.setUpdateTime(new Date());

        boolean save = this.save(sysRole);
        if(!save){
            throw new BusinessException("添加角色失败");
        }
        return Result.success();
    }

    @Override
    public Result<RoleForm> getRoleForm(Long id) {
        if (id == null){
            return Result.fail("参数错误");
        }
        SysRole sysRole = this.getById(id);
        RoleForm roleForm = new RoleForm();
        BeanUtils.copyProperties(sysRole,roleForm);
        return Result.success(roleForm);
    }

    @Override
    public Result updateRole(Long id, RoleForm roleForm) {
        if(id==null||roleForm==null){
            return Result.fail("参数错误");
        }
        SysRole sysRole = new SysRole();

        BeanUtils.copyProperties(roleForm, sysRole);

        sysRole.setUpdateTime(new Date()); // 更新时间

        boolean updated = this.updateById(sysRole);
        if (!updated) {
            throw new BusinessException("更新角色失败");
        }
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteRoles(List<Long> idList) {
        if(idList==null||idList.isEmpty()){
            return Result.fail("参数错误");
        }
        try {
            //先删除角色关联的菜单关系
            roleMenuService.remove(new QueryWrapper<SysRoleMenu>().in("role_id", idList));

            //再删除角色
//            boolean deleted = this.removeByIds(idList);
//            if (!deleted) {
//                throw new BusinessException("删除角色失败");
//            }
            boolean update = this.update(new UpdateWrapper<SysRole>().set("deleted", 1).in("id", idList));
            if (!update) {
                throw new BusinessException("删除角色失败");
            }
            return Result.success();
        } catch (Exception e) {
        log.error("删除角色失败");
        throw e;
    }
}

    @Override
    public Result<List<Long>> getRoleMenuIds(Long roleId) {
        if(roleId==null){
            return Result.fail("参数错误");
        }
        List<Long> list = roleMenuService.listObjs(new QueryWrapper<SysRoleMenu>().eq("role_id", roleId).select("menu_id"));
        return Result.success(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateRoleMenuIds(Long roleId, List<Long> menuIds) {
        if (!this.exists(new QueryWrapper<SysRole>().eq("id", roleId))){
            return Result.fail("角色不存在");
        }
        //添加关联
        List<SysRoleMenu> sysRoleMenus = menuIds.stream().filter(Objects::nonNull).map(menuId -> new SysRoleMenu(roleId, menuId)).collect(Collectors.toList());

        try {
            //删除旧关联
            roleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("role_id", roleId));

            if (!sysRoleMenus.isEmpty()){
                boolean flag = roleMenuService.saveBatch(sysRoleMenus);

                if (!flag) {
                    throw new BusinessException("更新角色菜单失败");
                }
            }

        }catch (Exception e){
            throw new BusinessException("更新角色菜单失败");
        }

        return Result.success();
    }

}
