package com.mall.backend.common.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class BaseEntity {
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    private Date updateTime;
}
