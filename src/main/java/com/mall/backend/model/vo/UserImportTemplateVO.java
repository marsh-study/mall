package com.mall.backend.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.mall.backend.model.form.UserForm;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@ColumnWidth(20)
public class UserImportTemplateVO {

        @ExcelProperty(value = "用户名")
        private String username;

        @ExcelProperty(value = "用户昵称")
        private String nickname;

        @ExcelProperty(value = "性别")
        private String gender;

        @ExcelProperty(value = "手机号码")
        private String mobile;

        @ExcelProperty(value = "邮箱")
        private String email;

        @ExcelProperty(value = "角色")
        @Schema(description = "角色ID列表，多个角色ID用逗号分隔")
        private String roleIds;

        public UserForm toUserForm() {
            UserForm userForm = new UserForm();
            userForm.setUsername(username);
            userForm.setNickname(nickname);
            userForm.setGender(gender!="男"?0:1);
            userForm.setMobile(mobile);
            userForm.setEmail(email);
            List<Long> roleIdList = Arrays.stream(roleIds.split(","))
                    .map(String::trim)
                    .map(Long::valueOf)
                    .toList();
            userForm.setRoleIds(roleIdList);
            userForm.setStatus(1);
            return userForm;
        }

}
