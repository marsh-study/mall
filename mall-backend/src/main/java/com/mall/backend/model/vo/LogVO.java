package com.mall.backend.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogVO {
/**
     * ID
     */
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

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
