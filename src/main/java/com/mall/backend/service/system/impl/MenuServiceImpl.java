package com.mall.backend.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.backend.common.model.OptionType;
import com.mall.backend.exception.BusinessException;
import com.mall.backend.mapper.system.MenuMapper;
import com.mall.backend.model.entity.*;
import com.mall.backend.model.form.MenuForm;
import com.mall.backend.model.query.MenuQuery;
import com.mall.backend.model.vo.MenuVO;
import com.mall.backend.model.vo.RouteVO;
import com.mall.backend.service.system.*;
import com.mall.backend.util.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, SysMenu> implements MenuService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<RouteVO> getRoutes(List<SysMenu> sysMenus) {
        List<RouteVO> routeList=getRouteTree(sysMenus);
        return routeList;
    }

    private List<RouteVO> getRouteTree(List<SysMenu> sysMenus) {
        List<RouteVO> routeList = new ArrayList<>();

        // 构建顶级菜单（parentId == 0）
        for (SysMenu sysMenu : sysMenus) {
            if (sysMenu.getParentId() == 0) {
                RouteVO routeVO = buildRouteVO(sysMenu, sysMenus);
                routeList.add(routeVO);
            }
        }

        return routeList;
    }

    // 新增辅助方法：递归构建 RouteVO 菜单树
    private RouteVO buildRouteVO(SysMenu parentMenu, List<SysMenu> allMenus) {
        RouteVO routeVO = new RouteVO();
        routeVO.setPath(parentMenu.getPath());
        routeVO.setComponent(parentMenu.getComponent());
        routeVO.setName(parentMenu.getName());
        routeVO.setRedirect(parentMenu.getRedirect());

        // 构造 meta 信息
        RouteVO.Meta meta = new RouteVO.Meta(
                parentMenu.getName(),           // title
                parentMenu.getIcon(),           // icon
                parentMenu.getVisible() != null && parentMenu.getVisible() == 0,  // hidden
                parentMenu.getKeepAlive() == null ? null : parentMenu.getKeepAlive() == 1, // keepAlive
                parentMenu.getAlwaysShow() != null && parentMenu.getAlwaysShow() == 1, // alwaysShow
                null                            // params 可按需扩展
        );
        routeVO.setMeta(meta);

        // 查找并递归添加子菜单
        List<RouteVO> children = new ArrayList<>();
        for (SysMenu childMenu : allMenus) {
            if (childMenu.getParentId() != null && childMenu.getParentId().equals(parentMenu.getId())) {
                children.add(buildRouteVO(childMenu, allMenus));
            }
        }
        routeVO.setChildren(children);

        return routeVO;
    }

    /**
     * 获取菜单下拉树形列表
     */
    @Override
    public Result<List<OptionType<String>>> getMenuOptions() {
        List<SysMenu> menuList = this.list();
        List<OptionType<String>> optionList = buildMenuTreeOptions(menuList, 0L);
        return Result.success(optionList);
    }


    /**
     * 递归构建菜单树形选项
     * @param menuList 所有菜单列表
     * @param parentId 父级ID
     * @return 树形选项列表
     */
    private List<OptionType<String>> buildMenuTreeOptions(List<SysMenu> menuList, Long parentId) {
        List<OptionType<String>> options = new ArrayList<>();

        for (SysMenu menu : menuList) {
            if (menu.getParentId() != null && menu.getParentId().equals(parentId)) {
                OptionType<String> option = new OptionType<>();
                option.setValue(menu.getId().toString());
                option.setLabel(menu.getName());

                // 递归构建子菜单选项
                List<OptionType<String>> children = buildMenuTreeOptions(menuList, menu.getId());
                if (!children.isEmpty()) {
                    option.setChildren(children);
                }

                options.add(option);
            }
        }

        return options;
    }

    @Override
    public Result<List<MenuVO>> listMenus(MenuQuery menuQuery) {
        try {
            QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.like(menuQuery.getKeywords() != null, "name", menuQuery.getKeywords());
            queryWrapper.orderByAsc("sort");
            List<SysMenu> list = this.list(queryWrapper);

            // 转换为 MenuVO 并建立 id -> MenuVO 映射
            List<MenuVO> menuVOList = list.stream()
                    .map(MenuVO::toMenuVO)
                    .collect(Collectors.toList());

            Map<Long, MenuVO> menuVOMap = menuVOList.stream()
                    .collect(Collectors.toMap(MenuVO::getId, Function.identity(), (v1, v2) -> v1));

            // 构建父子关系树
            List<MenuVO> result = new ArrayList<>();
            for (MenuVO menuVO : menuVOList) {
                Long parentId = menuVO.getParentId();

                if (parentId == null || !menuVOMap.containsKey(parentId)) {
                    // 是根节点
                    buildChildren(menuVO, menuVOMap);
                    result.add(menuVO);
                }
            }

            //菜单以及子菜单的排序

            return Result.success(result);
        } catch (Exception e) {
            log.error("获取菜单列表失败", e);
            return Result.fail("系统异常，请稍后再试");
        }
    }


    private void buildChildren(MenuVO parent, Map<Long, MenuVO> menuVOMap) {
        List<MenuVO> children = new ArrayList<>();

        for (MenuVO menuVO : menuVOMap.values()) {
            if (Objects.equals(menuVO.getParentId(), parent.getId())) {
                buildChildren(menuVO, menuVOMap); // 递归构建子节点
                children.add(menuVO);
            }
        }

        children.sort(Comparator.comparingInt(MenuVO::getSort));
        parent.setChildren(children);
    }

    @Override
    public Result addMenu(MenuForm menuForm) {
        SysMenu sysMenu = menuForm.toSysMenu();
        sysMenu.setTreePath(this.getTreePath(sysMenu.getParentId()));
        boolean flag = this.save(sysMenu);
        if (!flag) {
            throw new BusinessException("添加菜单失败");
        }
        return Result.success();
    }

    private String getTreePath(Long parentId) {
        if (parentId == null) {
            return "0";
        } else {
            SysMenu parentMenu = this.getById(parentId);
            if (parentMenu == null) {
                return "0";
            } else {
                return parentMenu.getTreePath() + "," + parentMenu.getId();
            }
        }
    }

    @Override
    public Result<MenuForm> getMenuForm(Long id) {
        SysMenu byId = this.getById(id);
        if (byId == null){
            return Result.fail("菜单不存在");
        }
        return Result.success(MenuForm.toMenuForm(byId));
    }

    @Override
    public Result updateMenu(Long id, MenuForm menuForm) {
        SysMenu sysMenu =menuForm.toSysMenu();
        sysMenu.setId(id);
        sysMenu.setTreePath(this.getTreePath(sysMenu.getParentId()));
        sysMenu.setUpdateTime(new Date());
        sysMenu.setCreateTime(null);

        boolean flag = this.updateById(sysMenu);
        if (!flag) {
            throw new BusinessException("修改菜单失败");
        }
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteMenus(String id) {
        //删除关联
        roleMenuService.remove(new QueryWrapper<SysRoleMenu>().in("menu_id", id));

        boolean deleted = this.removeById(id);
        return deleted ? Result.success() : Result.fail("删除菜单失败");
    }


}
