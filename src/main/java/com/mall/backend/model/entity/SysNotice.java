package com.mall.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.mall.backend.common.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * 通知公告表
 * </p>
 *
 * @author lsy
 * @since 2025-10-14
 */
@Getter
@Setter
@TableName("sys_notice")
@AllArgsConstructor
@NoArgsConstructor
public class SysNotice extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通知ID
     */
    @TableId(value = "notice_id", type = IdType.AUTO)
    private Long noticeId;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知类型（1通知 2公告）
     */
    private String type;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 发布人
     */
    private String publisher;

    /**
     * 发布人角色
     */
    private String publisherRole;

    /**
     * 目标角色（多个角色用逗号分隔）
     */
    private String targetRoles;

    /**
     * 目标用户（多个用户ID用逗号分隔）
     */
    private String targetUsers;

    /**
     * 通知等级（HIGH/MEDIUM/LOW）
     */
    private String level;

    /**
     * 发布状态（DRAFT草稿 PUBLISHED已发布）
     */
    private String status;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 创建者
     */
    private String createBy;


    /**
     * 更新者
     */
    private String updateBy;


    /**
     * 备注
     */
    private String remark;
}
