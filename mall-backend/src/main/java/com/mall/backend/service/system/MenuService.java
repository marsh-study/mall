package com.mall.backend.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.backend.common.model.OptionType;
import com.mall.backend.model.entity.SysMenu;
import com.mall.backend.model.form.MenuForm;
import com.mall.backend.model.query.MenuQuery;
import com.mall.backend.model.vo.MenuVO;
import com.mall.backend.model.vo.RouteVO;
import com.mall.backend.util.Result;

import java.util.List;

public interface MenuService extends IService<SysMenu> {
    List<RouteVO> getRoutes(List<SysMenu> sysMenus);

    Result<List<OptionType<String>>> getMenuOptions();

    Result<List<MenuVO>> listMenus(MenuQuery menuQuery);

    Result addMenu(MenuForm menuForm);

    Result<MenuForm> getMenuForm(Long id);

    Result updateMenu(Long id, MenuForm menuForm);

    Result deleteMenus(String id);
}
