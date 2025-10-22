package com.mall.backend.controller;

import com.mall.backend.annotation.SystemLog;
import com.mall.backend.common.model.OptionType;
import com.mall.backend.model.entity.SysMenu;
import com.mall.backend.model.entity.SysRole;
import com.mall.backend.model.entity.SysUser;
import com.mall.backend.model.form.MenuForm;
import com.mall.backend.model.query.MenuQuery;
import com.mall.backend.model.vo.MenuVO;
import com.mall.backend.util.Result;
import com.mall.backend.model.vo.RouteVO;
import com.mall.backend.service.system.MenuService;
import com.mall.backend.service.system.RoleService;
import com.mall.backend.service.system.UserService;
import com.mall.backend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RequestMapping("/menus")
@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RoleService roleService;

    @GetMapping("/routes")
    public Result routes(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        String username = jwtUtil.getClaim(accessToken.replaceFirst("^Bearer ", ""), Claims::getSubject);
        SysUser user = userService.getUserByUsername(username);
        List<Long> roleIds = user.getRoles().stream().map(SysRole::getId).collect(Collectors.toList());
        List<SysMenu> sysMenus = roleService.getPermsByRoleIds(roleIds);
        List<RouteVO> routes = menuService.getRoutes(sysMenus);
        return Result.success(routes);
    }


    @GetMapping("/options")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<OptionType<String>>> getMenuOptions(){
        return menuService.getMenuOptions();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Result<List<MenuVO>> listMenus(MenuQuery menuQuery){
        return menuService.listMenus(menuQuery);
    }

    /**
     * 新增菜单
     * @param menuForm
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @SystemLog(module = "菜单管理", operation = "添加菜单")
    public Result addMenu(@RequestBody MenuForm menuForm){
        if(menuForm == null){
            return Result.fail("参数错误");
        }
        return menuService.addMenu(menuForm);
    }

    /**
     * 获取菜单表单
     * @param id
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping ("/{id}/form")
    public Result<MenuForm> getMenuForm(@PathVariable Long id){
        if(id == null){
            return Result.fail("参数错误");
        }
        return menuService.getMenuForm(id);
    }

    /**
     * 修改菜单
     * @param id
     * @param menuForm
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @SystemLog(module = "菜单管理", operation = "修改菜单")
    public Result updateMenu(@PathVariable Long id, @RequestBody MenuForm menuForm){
        if(id == null || menuForm == null){
            return Result.fail("参数错误");
        }
        return menuService.updateMenu(id, menuForm);
    }

    /**
     * 删除菜单
     * @param ids
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @SystemLog(module = "菜单管理", operation = "删除菜单")
    public Result deleteMenus(@PathVariable String id){
        if (id == null){
            return Result.fail("参数错误");
        }
        return menuService.deleteMenus(id);
    }
}
