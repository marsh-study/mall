package com.mall.backend.model.vo;

import com.mall.backend.model.entity.SysNotice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticePageVO {
/**
     * 通知ID
     */
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
    private java.util.Date publishTime;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private java.util.Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private java.util.Date updateTime;

    /**
     * 备注
     */
    private String remark;

    public static NoticePageVO fromEntity(SysNotice notice) {
        NoticePageVO vo = new NoticePageVO();
        vo.setNoticeId(notice.getNoticeId());
        vo.setTitle(notice.getTitle());
        vo.setType(notice.getType());
        vo.setContent(notice.getContent());
        vo.setPublisher(notice.getPublisher());
        vo.setPublisherRole(notice.getPublisherRole());
        vo.setTargetRoles(notice.getTargetRoles());
        vo.setTargetUsers(notice.getTargetUsers());
        vo.setLevel(notice.getLevel());
        vo.setStatus(notice.getStatus());
        vo.setPublishTime(notice.getPublishTime());
        vo.setCreateBy(notice.getCreateBy());
        vo.setCreateTime(notice.getCreateTime());
        vo.setUpdateBy(notice.getUpdateBy());
        vo.setUpdateTime(notice.getUpdateTime());
        vo.setRemark(notice.getRemark());
        return vo;
    }
}
