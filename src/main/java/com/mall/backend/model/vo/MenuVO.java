package com.mall.backend.model.vo;

import com.mall.backend.enums.MenuTypeEnum;
import com.mall.backend.model.entity.SysMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 菜单视图对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuVO {
    /**
     * 子菜单
     */
    private List<MenuVO> children;
    /**
     * 组件路径
     */
    private String component;
    /**
     * ICON
     */
    private String icon;
    /**
     * 菜单ID
     */
    private Long id;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 父菜单ID
     */
    private Long parentId;
    /**
     * 按钮权限标识
     */
    private String perm;
    /**
     * 跳转路径
     */
    private String redirect;
    /**
     * 路由名称
     */
    private String routeName;
    /**
     * 路由相对路径
     */
    private String routePath;
    /**
     * 菜单排序(数字越小排名越靠前)
     */
    private Integer sort;
    /**
     * 菜单类型
     */
    private String type;
    /**
     * 菜单是否可见(1:显示;0:隐藏)
     */
    private Integer visible;


    public static MenuVO toMenuVO(SysMenu sysMenu) {
        MenuVO menuVO = new MenuVO();

        menuVO.setId(sysMenu.getId());
        menuVO.setName(sysMenu.getName());
        menuVO.setParentId(sysMenu.getParentId());
        menuVO.setPerm(sysMenu.getPerm());
        menuVO.setRedirect(sysMenu.getRedirect());
        menuVO.setRouteName(sysMenu.getName());
        menuVO.setRoutePath(sysMenu.getPath());
        menuVO.setSort(sysMenu.getSort());
        menuVO.setVisible(sysMenu.getVisible());
        menuVO.setComponent(sysMenu.getComponent());
        menuVO.setIcon(sysMenu.getIcon());
        menuVO.setType(MenuTypeEnum.getDescriptionByCode(sysMenu.getType()));
        return menuVO;
    }
}
