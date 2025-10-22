package com.mall.backend.model.entity;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author lsy
 * @since 2025-10-13
 */
@Data
@TableName("sys_log")
@AllArgsConstructor
@NoArgsConstructor
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作人
     */
    private String username;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 模块名
     */
    private String module;

    /**
     * 操作名
     */
    private String operation;

    /**
     * 方法名
     */
    private String method;

    /**
     * 请求URL
     */
    private String url;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 结果 SUCCESS/FAIL
     */
    private String result;

    /**
     * 错误信息
     */
    private String errorMsg;

    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
