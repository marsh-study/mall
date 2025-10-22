package com.mall.backend.service.system.impl;



import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.backend.enums.GenderEnum;
import com.mall.backend.mapper.system.UserMapper;
import com.mall.backend.model.entity.SysMenu;
import com.mall.backend.model.entity.SysRole;
import com.mall.backend.model.entity.SysUser;
import com.mall.backend.model.entity.SysUserRole;
import com.mall.backend.model.form.UserForm;
import com.mall.backend.model.query.UserPageQuery;
import com.mall.backend.model.vo.CurrentUserVO;
import com.mall.backend.model.vo.UserExportVO;
import com.mall.backend.model.vo.UserPageVO;
import com.mall.backend.service.system.RoleService;
import com.mall.backend.service.system.UserRoleService;
import com.mall.backend.service.system.UserService;
import com.mall.backend.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements UserService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;


    @Autowired
    private PasswordEncoder passwordEncoder;



     @Override
    public CurrentUserVO getCurrentUser(String username, List<String> roles) {

        SysUser user = getUserByUsername(username);
        CurrentUserVO currentUserVO =new CurrentUserVO();
        currentUserVO.setUserId(user.getId());
        currentUserVO.setUsername(user.getUsername());
        currentUserVO.setNickname(user.getNickname());
        currentUserVO.setAvatar(user.getAvatar());
        currentUserVO.setRoles(roles);
        currentUserVO.setGender(user.getGender());
        currentUserVO.setMobile(user.getMobile());
        currentUserVO.setEmail(user.getEmail());
        currentUserVO.setStatus(user.getStatus());

        // TODO: 从数据库查询用户权限
        List<Long> roleIds = user.getRoles().stream().map(SysRole::getId).collect(Collectors.toList());
        List<SysMenu> sysMenus = roleService.getPermsByRoleIds(roleIds);

        currentUserVO.setPerms(sysMenus.stream().map(SysMenu::getPerm).filter(perm -> perm != null).collect(Collectors.toList()));

        return currentUserVO;
    }

    @Override
    public SysUser getUserByUsername(String username) {
        SysUser user = this.getOne(new QueryWrapper<SysUser>().eq("username", username));
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setRoles(getRolesByUserId(user.getId()));

        return user;
    }

    @Override
    public PageResult<UserPageVO> getUserPage(UserPageQuery userPageQuery) {
        // 1. 分页查询用户列表
        Page<SysUser> sysUserPage = new Page<>(userPageQuery.getPageNum(), userPageQuery.getPageSize());
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        if (userPageQuery.getKeywords() != null) {
            queryWrapper.like("username", userPageQuery.getKeywords())
                    .or()
                    .like("nickname", userPageQuery.getKeywords())
                    .or()
                    .like("mobile", userPageQuery.getKeywords());
        }
        queryWrapper.eq(userPageQuery.getStatus() != null, "status", userPageQuery.getStatus())
                .eq("deleted", 0);
        Page<SysUser> page = this.page(sysUserPage, queryWrapper);

        // 批量获取所有用户的角色
        List<Long> userIds = page.getRecords().stream().map(SysUser::getId).collect(Collectors.toList());
        Map<Long, List<SysRole>> userRolesMap = userRoleService.getRolesByUserIds(userIds);

        // 2. 使用 Stream API 转换 SysUser -> UserPageVO
        List<UserPageVO> pageVOList = page.getRecords().stream()
                .map(sysUser -> {
                    UserPageVO userPageVO = new UserPageVO();
                    userPageVO.setAvatar(sysUser.getAvatar());
                    userPageVO.setEmail(sysUser.getEmail());
                    userPageVO.setGenderLabel(GenderEnum.getMessageByCode(sysUser.getGender()));

                    String roleNames = userRolesMap.getOrDefault(sysUser.getId(), List.of()).stream()
                            .map(SysRole::getName)
                            .collect(Collectors.joining(","));
                    userPageVO.setRoleNames(roleNames); // 提取角色名称查询

                    userPageVO.setId(sysUser.getId());
                    userPageVO.setUsername(sysUser.getUsername());
                    userPageVO.setNickname(sysUser.getNickname());
                    userPageVO.setMobile(sysUser.getMobile());
                    userPageVO.setStatus(sysUser.getStatus());
                    userPageVO.setCreateTime(sysUser.getCreateTime());
                    return userPageVO;
                })
                .collect(Collectors.toList());
        // 3. 返回分页结果（包含总记录数）
        return new PageResult<>(pageVOList, page.getTotal());
    }

    /**
     * 添加用户
     * @param userForm 用户表单
     * @return 是否添加成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(UserForm userForm) {
        // 从安全上下文中获取当前登录用户id
        Long currentUserId = getCurrentUserId();

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userForm,sysUser);
        sysUser.setDeleted(0);
        sysUser.setCreateBy(currentUserId);
        sysUser.setUpdateBy(currentUserId);
        sysUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
        sysUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        if(!this.save(sysUser)){
            return false;
        }

        List<SysUserRole> sysUserRoles = userForm.getRoleIds().stream()
                .map(roleId -> new SysUserRole(sysUser.getId(), roleId))
                .collect(Collectors.toList());
        return userRoleService.saveBatch(sysUserRoles);
    }

    /**
     * 删除用户
     * @param ids 用户id列表，逗号分隔
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(String ids) {
        // 1. 校验参数是否为空
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        // 2. 解析用户id列表（逗号分隔）
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        //检查列表中是否有自己
        if(idList.contains(getCurrentUserId())){
            return false;
        }

        // 3. 批量删除用户角色关联
        if(userRoleService.exists(new QueryWrapper<SysUserRole>().in("user_id", idList))){
            if(!userRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", idList))){
                return false;
            }
        }

        // 4. 批量删除用户(修改deleted)
//        if (!this.removeByIds(idList)) {
//            return false;
//        }
        if (!this.update(new UpdateWrapper<SysUser>().set("deleted", 1).in("id", idList))) {
            return false;
        }

        return true;
    }

    @Override
    public UserForm getUserForm(Long userId) {
        // 1. 查询用户信息
        SysUser sysUser = this.getById(userId);
        if (sysUser == null) {
            return null; // 用户不存在时返回 null
        }

        // 2. 转换为 UserForm
        UserForm userForm = new UserForm();
        BeanUtils.copyProperties(sysUser, userForm);

        // 3. 查询用户角色关联并设置 roleIds
        List<SysRole> rolesByUserId = getRolesByUserId(userId);
        if (rolesByUserId==null){
            userForm.setRoleIds(Collections.emptyList());
        }else {
            userForm.setRoleIds(rolesByUserId.stream()
                .map(SysRole::getId)
                .collect(Collectors.toList()));
        }
        return userForm;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(UserForm userForm) {
        // 1. 校验参数是否为空
        if (userForm == null || userForm.getId() == null) {
            return false;
        }

        // 2. 校验用户是否存在
        SysUser sysUser = this.getById(userForm.getId());
        if (sysUser == null) {
            return false; // 用户不存在时返回 false
        }

        // 3. 更新用户信息
        BeanUtils.copyProperties(userForm, sysUser);
        sysUser.setUpdateBy(getCurrentUserId());
        sysUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        if (!this.updateById(sysUser)) {
            return false; // 更新失败时返回 false
        }

        if(userForm.getRoleIds()==null){
            return true;
        }
        // 4. 批量删除旧角色关联
        if(userRoleService.exists(new QueryWrapper<SysUserRole>().eq("user_id", userForm.getId()))){
            if(!userRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", userForm.getId()))){
                return false; // 删除旧关联失败时返回 false
            }
        }

        // 5. 如果有新角色则批量添加
        if (userForm.getRoleIds() != null && !userForm.getRoleIds().isEmpty()) {
            List<SysUserRole> sysUserRoles = userForm.getRoleIds().stream()
                    .map(roleId -> new SysUserRole(userForm.getId(), roleId))
                    .collect(Collectors.toList());
            return userRoleService.saveBatch(sysUserRoles);
        }
                // 无新角色时返回 true
        return true;
    }

    @Override
    public boolean updateUserStatus(Long userId, Integer status) {
        // 1. 校验参数是否为空
        if (userId == null || status == null) {
            return false;
        }

        // 2. 校验用户是否存在
        SysUser sysUser = this.getById(userId);
        if (sysUser == null) {
            return false; // 用户不存在时返回 false
        }

        // 3. 更新用户状态
        sysUser.setStatus(status);
        sysUser.setUpdateBy(getCurrentUserId());
        sysUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return this.updateById(sysUser);
    }

    @Override
    public boolean updateUserPassword(Long userId, String password) {
        // 1. 校验参数是否为空
        if (userId == null || password == null) {
            return false;
        }

        // 2. 校验用户是否存在
        SysUser sysUser = this.getById(userId);
        if (sysUser == null) {
            return false; // 用户不存在时返回 false
        }

        // 3. 更新用户密码
        sysUser.setPassword(passwordEncoder.encode(password));
        sysUser.setUpdateBy(getCurrentUserId());
        sysUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return this.updateById(sysUser);
    }

    @Override
    public List<UserExportVO> getUserExportVO(UserPageQuery userPageQuery) {
        if (userPageQuery == null) {
            return null; // 参数为空时返回 null
        }


        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        if(userPageQuery.getKeywords()!=null){
            queryWrapper.like("username", userPageQuery.getKeywords())
                    .or().like("mobile", userPageQuery.getKeywords())
                    .or().like("nickname", userPageQuery.getKeywords());
        }

        queryWrapper.eq(userPageQuery.getStatus()!=null, "status", userPageQuery.getStatus());

        List<SysUser> userList = null;
        if (userPageQuery.getPageNum() == null || userPageQuery.getPageSize() == null) {
            userList = this.list(queryWrapper);
        } else {
            userList = this.list(new Page<>(userPageQuery.getPageNum(), userPageQuery.getPageSize()), queryWrapper);
        }
        log.info("userList:{}", userList);
        List<UserExportVO> userExportVOList = new ArrayList<>();
        for (SysUser user : userList) {
            UserExportVO userExportVO = new UserExportVO();
            userExportVO.setUsername(user.getUsername());
            userExportVO.setNickname(user.getNickname());
            userExportVO.setGender(user.getGender()==1?"男":"女");
            userExportVO.setMobile(user.getMobile());
            userExportVO.setEmail(user.getEmail());
            // 修复创建时间字段赋值
            if (user.getCreateTime() != null) {
                userExportVO.setCreateTime(new Date(user.getCreateTime().getTime()));
            }
            userExportVOList.add(userExportVO);
        }


        return userExportVOList;
    }

    /**
     * 根据用户id查询角色列表
     * @param userId 用户id
     * @return 角色列表
     */
    private List<SysRole> getRolesByUserId(Long userId) {
        // 查询用户角色关联
        List<SysUserRole> userRoles = userRoleService.list(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        if (userRoles.isEmpty()) {
            return null; // 无角色时返回 null
        }

        // 查询角色名称并拼接（使用逗号分隔，替代 List.toString()）
        List<Long> roleIds = userRoles.stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());

        return roleService.listByIds(roleIds);
    }

    public Long getCurrentUserId(){
        // 从安全上下文中获取当前登录用户id
        String currentUserName= SecurityContextHolder.getContext().getAuthentication().getName();
        SysUser currentUser = this.getOne(new QueryWrapper<SysUser>().eq("username", currentUserName));
        if (currentUser == null) {
            return null; // 当前用户不存在时返回 null
        }
        Long currentUserId = currentUser.getId();
        return currentUserId;
    }

    @Override
    public boolean verifyPassword(Long id, String oldPassword) {
        SysUser user = this.getById(id);
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    public String getCurrentUserName(){
        // 从安全上下文中获取当前登录用户UserName
        String currentUserName= SecurityContextHolder.getContext().getAuthentication().getName();
        return currentUserName;
    }
}
