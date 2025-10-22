package com.mall.backend.model.form;

import com.mall.backend.model.entity.SysNotice;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeForm {
/**
     * 通知ID
     */
    private Long noticeId;

    /**
     * 通知标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 通知类型（1通知 2公告）
     */
    @NotBlank(message = "类型不能为空")
    private String type;

    /**
     * 通知内容
     */
    @NotBlank(message = "内容不能为空")
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
    private java.util.Date publishTime;

    /**
     * 备注
     */
    private String remark;

    public SysNotice toEntity() {
        SysNotice entity = new SysNotice();
        entity.setNoticeId(this.noticeId);
        entity.setTitle(this.title);
        entity.setType(this.type);
        entity.setContent(this.content);
        entity.setPublisher(this.publisher);
        entity.setPublisherRole(this.publisherRole);
        entity.setTargetRoles(this.targetRoles);
        entity.setTargetUsers(this.targetUsers);
        entity.setLevel(this.level);
        entity.setStatus(this.status);
        entity.setPublishTime(this.publishTime);
        entity.setRemark(this.remark);
        entity.setCreateTime(new java.util.Date());
        entity.setUpdateTime(new java.util.Date());
        return entity;
    }

    public SysNotice toEntity(Date updateTime){
        SysNotice entity = new SysNotice();
        entity.setNoticeId(this.noticeId);
        entity.setTitle(this.title);
        entity.setType(this.type);
        entity.setContent(this.content);
        entity.setPublisher(this.publisher);
        entity.setPublisherRole(this.publisherRole);
        entity.setTargetRoles(this.targetRoles);
        entity.setTargetUsers(this.targetUsers);
        entity.setLevel(this.level);
        entity.setStatus(this.status);
        entity.setPublishTime(this.publishTime);
        entity.setRemark(this.remark);
        entity.setCreateTime(new java.util.Date());
        return entity;
    }
}
