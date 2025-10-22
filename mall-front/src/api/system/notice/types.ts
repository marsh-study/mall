/**
 * 通知公告查询参数类型
 */
export interface NoticeQuery extends PageQuery {
  title?: string;
  type?: string;
  publisher?: string;
  level?: string;
  status?: string;
}

/**
 * 通知公告分页列表项类型
 */
export interface NoticePageVO {
  /**
   * 通知ID
   */
  noticeId?: number;

  /**
   * 通知标题
   */
  title?: string;

  /**
   * 通知类型（1通知 2公告）
   */
  type?: string;

  /**
   * 通知内容
   */
  content?: string;

  /**
   * 发布人
   */
  publisher?: string;

  /**
   * 发布人角色
   */
  publisherRole?: string;

  /**
   * 目标角色（多个角色用逗号分隔）
   */
  targetRoles?: string;

  /**
   * 目标用户（多个用户ID用逗号分隔）
   */
  targetUsers?: string;

  /**
   * 通知等级（HIGH/MEDIUM/LOW）
   */
  level?: string;

  /**
   * 发布状态（DRAFT草稿 PUBLISHED已发布）
   */
  status?: string;

  /**
   * 发布时间
   */
  publishTime?: Date;

  /**
   * 创建者
   */
  createBy?: string;

  /**
   * 创建时间
   */
  createTime?: Date;

  /**
   * 更新者
   */
  updateBy?: string;

  /**
   * 更新时间
   */
  updateTime?: Date;

  /**
   * 备注
   */
  remark?: string;
}

/**
 * 通知公告表单类型
 */
export interface NoticeForm {
  /**
   * 通知ID
   */
  noticeId?: number;

  /**
   * 通知标题
   */
  title: string;

  /**
   * 通知类型（1通知 2公告）
   */
  type: string;

  /**
   * 通知内容
   */
  content?: string;

  /**
   * 发布人
   */
  publisher: string;

  /**
   * 发布人角色
   */
  publisherRole?: string;

  /**
   * 目标角色（多个角色用逗号分隔）
   */
  targetRoles?: string[];

  /**
   * 目标用户（多个用户ID用逗号分隔）
   */
  targetUsers?: string[];

  /**
   * 通知等级（HIGH/MEDIUM/LOW）
   */
  level: string;

  /**
   * 发布状态（DRAFT草稿 PUBLISHED已发布）
   */
  status: string;

  /**
   * 发布时间
   */
  publishTime?: Date;

  /**
   * 备注
   */
  remark?: string;
}

/**
 * 通知公告详情类型
 */
export interface NoticeDetailVO {
  id: string;
  title: string;
  content: string;
  publisherName: string;
  publishTime: string;
  isRead?: number;
  [key: string]: any;
}
