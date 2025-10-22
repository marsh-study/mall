package com.mall.backend.controller;

import com.alibaba.excel.EasyExcel;
import com.mall.backend.annotation.SystemLog;
import com.mall.backend.exception.BusinessException;
import com.mall.backend.model.entity.SysUser;
import com.mall.backend.model.form.UserForm;
import com.mall.backend.model.query.UserPageQuery;
import com.mall.backend.model.vo.CurrentUserVO;
import com.mall.backend.model.vo.UserExportVO;
import com.mall.backend.model.vo.UserImportTemplateVO;
import com.mall.backend.model.vo.UserPageVO;
import com.mall.backend.service.system.UserService;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前用户信息
     *
     * @return Result<CurrentUserVO>
     */
    @RequestMapping("/me")
    public Result me() {
        // 从安全上下文中获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String username = authentication.getName();
        try {
            CurrentUserVO currentUserVO = userService.getCurrentUser(username, roles);
            return Result.success(currentUserVO);
        } catch (Exception e) {
            throw new BusinessException("获取用户信息失败");
        }
    }

    /**
     * 获取用户分页信息
     *
     * @param userPageQuery 用户分页查询参数
     * @return Result<PageResult < UserPageVO>>
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/page")
    public Result<PageResult<UserPageVO>> getUserPage(UserPageQuery userPageQuery) {
        PageResult<UserPageVO> userPageVOList = userService.getUserPage(userPageQuery);
        if (userPageVOList == null) {
            throw new BusinessException("用户分页信息为空");
        }
        return Result.success(userPageVOList);
    }

    /**
     * 添加用户
     *
     * @param userForm 用户表单
     * @return Result
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Schema(description = "添加用户", example = "UserForm")
    @SystemLog(module = "用户管理", operation = "添加用户")
    public Result addUser(@RequestBody UserForm userForm) {
        log.info("添加用户请求参数: {}", userForm);
        boolean isSuccess = userService.addUser(userForm);
        if (!isSuccess) {
            throw new BusinessException("添加用户失败");
        }
        return Result.success();
    }

    /**
     * 更新用户
     *
     * @param userId   用户ID
     * @param userForm 用户表单
     * @return Result
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    @Schema(description = "更新用户", example = "UserForm")
    @SystemLog(module = "用户管理", operation = "更新用户")
    public Result updateUser(@PathVariable("userId") Long userId, @RequestBody UserForm userForm) {
        log.info("更新用户请求参数: {}", userForm);
        userForm.setId(userId);
        boolean isSuccess = userService.updateUser(userForm);
        if (!isSuccess) {
            throw new BusinessException("更新用户失败");
        }
        return Result.success();
    }

    /**
     * 删除用户
     *
     * @param ids 用户ID列表
     * @return Result
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{ids}")
    @Schema(description = "删除用户", example = "ids=1,2,3")
    @SystemLog(module = "用户管理", operation = "删除用户")
    public Result deleteUser(@PathVariable("ids") String ids) {
        log.info("删除用户请求参数: {}", ids);
        boolean isSuccess = userService.deleteUser(ids);
        if (!isSuccess) {
            throw new BusinessException("删除用户失败");
        }
        return Result.success();
    }

    /**
     * 根据用户id获取用户表单
     *
     * @param userId 用户ID
     * @return Result<UserForm>
     */
    @GetMapping("/{userId}/form")
    @PreAuthorize("hasRole('ADMIN')")
    @Schema(description = "根据用户id获取用户表单", example = "userId=1")
    public Result<UserForm> getUserForm(@PathVariable("userId") Long userId) {
        log.info("根据用户id获取用户表单请求参数: {}", userId);
        UserForm userForm = userService.getUserForm(userId);
        if (userForm == null) {
            throw new BusinessException("根据用户id获取用户表单失败");
        }
        return Result.success(userForm);
    }

    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 用户状态(1:正常;0:禁用)
     * @return Result
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}/status")
    @Schema(description = "更新用户状态", example = "userId=1,status=0")
    @SystemLog(module = "用户管理", operation = "更新用户状态")
    public Result updateUserStatus(@PathVariable("userId") Long userId, @RequestParam("status") Integer status) {
        log.info("更新用户状态请求参数: userId={}, status={}", userId, status);
        boolean isSuccess = userService.updateUserStatus(userId, status);
        if (!isSuccess) {
            throw new BusinessException("更新用户状态失败");
        }
        return Result.success();
    }

    /**
     * 修改用户密码
     *
     * @param userId   用户ID
     * @param password 密码
     * @return Result
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}/password")
    @Schema(description = "修改用户密码", example = "userId=1,password=123456")
    @SystemLog(module = "用户管理", operation = "修改用户密码")
    public Result updateUserPassword(@PathVariable("userId") Long userId, @RequestParam("password") String password) {
        log.info("修改用户密码请求参数: userId={}, password={}", userId, password);
        boolean isSuccess = userService.updateUserPassword(userId, password);
        if (!isSuccess) {
            throw new BusinessException("修改用户密码失败");
        }
        return Result.success();
    }

    /**
     * 导出用户数据
     *
     * @return Result
     */
    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    @Schema(description = "导出用户数据", example = "export")
    @SystemLog(module = "用户管理", operation = "导出用户数据")
    public ResponseEntity<byte[]> exportUser(UserPageQuery userPageQuery) {
        log.info("导出用户数据请求参数: {}", userPageQuery);
        try {
            // 1. 调用服务层获取导出数据
            List<UserExportVO> userExportVOList = userService.getUserExportVO(userPageQuery);
            if (userExportVOList == null) {
                throw new BusinessException("导出用户数据失败");
            }
            //使用 EasyExcel 生成 Excel 并写入字节数组
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            EasyExcel.write(out, UserExportVO.class)
                    .sheet("用户列表")
                    .doWrite(userExportVOList);

            // 2. 构建文件名（根据导出类型动态拼接扩展名)
            String fileName = "用户数据_" + System.currentTimeMillis() + ".xlsx";

            // 3. 通过 ResponseUtil 返回文件流响应（核心改造点）
            return Result.exportFile(out.toByteArray(), fileName);

        } catch (Exception e) {
            // 4. 异常时通过 ResponseUtil 返回错误响应
            return Result.error("导出失败：" + e.getMessage());
        }

        }

    /**
     * 下载导入用户模板
     *
     * @return ResponseEntity<byte[]>
     */
    @GetMapping("/template")
    @PreAuthorize("hasRole('ADMIN')")
    @Schema(description = "下载导入用户模板", example = "template")
    @SystemLog(module = "用户管理", operation = "下载导入用户模板")
    public ResponseEntity<byte[]> downloadTemplateApi() {
        log.info("下载导入用户模板");
        try {

            // 使用 EasyExcel 生成 Excel 模板并写入字节数组
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            EasyExcel.write(out, UserImportTemplateVO.class)
                    .sheet("用户导入模板")
                    .doWrite(List.of()); // 提供一个空列表作为数据源

            // 构建文件名
            String fileName = "用户导入模板.xlsx";

            // 返回文件流响应
            return Result.exportFile(out.toByteArray(), fileName);

        } catch (Exception e) {
            log.error("下载导入用户模板失败", e);
            return Result.error("模板下载失败：" + e.getMessage());
        }
    }

    /**
     * 导入用户数据
     *
     * @param file 导入文件
     * @return Result
     */
    @PostMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    @Schema(description = "导入用户数据", example = "import")
    @SystemLog(module = "用户管理", operation = "导入用户数据")
    public Result importUser(@RequestParam("file") MultipartFile file) {
        log.info("导入用户数据请求参数: {}", file);
        try {
            // 检查文件是否为空
            if (file == null || file.isEmpty()) {
                return Result.fail("上传文件不能为空");
            }

            // 使用EasyExcel读取Excel文件内容
            List<UserImportTemplateVO> dataList = EasyExcel.read(file.getInputStream())
                    .head(UserImportTemplateVO.class)
                    .sheet()
                    .doReadSync();

            // 检查是否有数据
            if (dataList == null || dataList.isEmpty()) {
                return Result.fail("文件中没有数据");
            }

            List<UserForm> list = dataList.stream().map(UserImportTemplateVO::toUserForm).toList();

            // 处理导入的数据
            int successCount = 0;
            int failCount = 0;
            StringBuilder errorMsg = new StringBuilder();

            for (int i = 0; i < list.size(); i++) {
                UserForm userData = list.get(i);
                try {
                    userService.addUser(userData);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    errorMsg.append("第").append(i + 1).append("行数据导入失败：").append(e.getMessage()).append("; ");
                }
            }

            String resultMsg = String.format("导入完成，成功：%d条，失败：%d条", successCount, failCount);
            if (failCount > 0) {
                resultMsg += "，错误信息：" + errorMsg.toString();
            }
            log.info(resultMsg);
            return Result.success(resultMsg);
        } catch (Exception e) {
            log.error("导入用户数据失败", e);
            return Result.fail("导入失败：" + e.getMessage());
        }
    }

    /**
     * 用户修改自己的密码（包含旧密码验证）
     *
     * @param id 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @PatchMapping("/{id}/password/self")
    public Result updateUserPasswordSelf(@PathVariable("id") Long id, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
        log.info("用户修改密码请求参数: id={}, oldPassword={}, newPassword={}", id, oldPassword, newPassword);
        // 1. 获取当前用户
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return Result.fail("用户未登录");
        }
        if(!currentUserId.equals(id)){
            return Result.fail("修改失败");
        }
        // 2. 校验旧密码
        if (!userService.verifyPassword(id, oldPassword)) {
            return Result.fail("旧密码错误");
        }
        boolean isSuccess = userService.updateUserPassword(id, newPassword);
        if (!isSuccess) {
            throw new BusinessException("修改用户密码失败");
        }
        return Result.success();
    }


}