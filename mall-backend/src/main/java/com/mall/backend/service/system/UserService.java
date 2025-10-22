package com.mall.backend.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.backend.model.entity.SysUser;
import com.mall.backend.model.form.UserForm;
import com.mall.backend.model.query.UserPageQuery;
import com.mall.backend.model.vo.CurrentUserVO;
import com.mall.backend.model.vo.UserExportVO;
import com.mall.backend.model.vo.UserPageVO;
import com.mall.backend.util.PageResult;

import java.util.List;

public interface UserService extends IService<SysUser> {
    CurrentUserVO getCurrentUser(String username, List<String> roles);

    SysUser getUserByUsername(String username);

    PageResult<UserPageVO> getUserPage(UserPageQuery userPageQuery);

    boolean addUser(UserForm userForm);

    boolean deleteUser(String ids);

    UserForm getUserForm(Long userId);

    boolean updateUser(UserForm userForm);

    boolean updateUserStatus(Long userId, Integer status);

    boolean updateUserPassword(Long userId, String password);

    List<UserExportVO> getUserExportVO(UserPageQuery userPageQuery);

    Long getCurrentUserId();

    boolean verifyPassword(Long id, String oldPassword);
}
