package com.mall.backend.model.form;

import com.mall.backend.controller.MenuController;
import com.mall.backend.enums.MenuTypeEnum;
import com.mall.backend.model.entity.SysMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuForm {
    /**
     * 菜单ID
     */
    private Long id;
    /**
     * 父菜单ID
     */
    private Long parentId;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 菜单是否可见(1:是;0:否;)
     */
    private Integer visible;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 组件路径
     */
    private String component;
    /**
     * 路由路径
     */
    private String path;
    /**
     * 跳转路由路径
     */
    private String redirect;
    /**
     * 菜单类型
     */
    private String type;
    /**
     * 权限标识
     */
    private String perm;
    /**
     * 【菜单】是否开启页面缓存
     */
    private Integer keepAlive;
    /**
     * 【目录】只有一个子路由是否始终显示
     */
    private Integer alwaysShow;


    public SysMenu toSysMenu(){ 
        SysMenu sysMenu = new SysMenu();
        sysMenu.setParentId(parentId);
        sysMenu.setType(MenuTypeEnum.getCodeByDescription(type));
        sysMenu.setName(name);
        sysMenu.setPath(path);
        sysMenu.setComponent(component);
        sysMenu.setPerm(perm);
        sysMenu.setIcon(icon);
        sysMenu.setSort(sort);
        sysMenu.setVisible(visible);
        sysMenu.setRedirect(redirect);
        sysMenu.setKeepAlive(keepAlive);
        sysMenu.setAlwaysShow(alwaysShow);
        sysMenu.setCreateTime(new Date());
        sysMenu.setUpdateTime(new Date());
        return sysMenu;
    }

    public static MenuForm toMenuForm(SysMenu sysMenu){
        MenuForm menuForm = new MenuForm();
        menuForm.setId(sysMenu.getId());
        menuForm.setParentId(sysMenu.getParentId());
        menuForm.setName(sysMenu.getName());
        menuForm.setVisible(sysMenu.getVisible());
        menuForm.setIcon(sysMenu.getIcon());
        menuForm.setSort(sysMenu.getSort());
        menuForm.setComponent(sysMenu.getComponent());
        menuForm.setPath(sysMenu.getPath());
        menuForm.setRedirect(sysMenu.getRedirect());
        menuForm.setType(MenuTypeEnum.getDescriptionByCode(sysMenu.getType()));
        menuForm.setPerm(sysMenu.getPerm());
        menuForm.setKeepAlive(sysMenu.getKeepAlive());
        menuForm.setAlwaysShow(sysMenu.getAlwaysShow());
        return menuForm;
    }
}
