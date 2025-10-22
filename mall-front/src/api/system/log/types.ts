/**
 * 日志查询参数类型
 */
export interface LogQuery extends PageQuery {
  /**
   * 模块名
   */
  module?: string;

  /**
   * 操作人
   */
  username?: string;

  /**
   * 时间范围
   */
  createTime?: string[];
  /**
   * 开始时间
   */
  startTime?: string;
  /**
   * 结束时间
   */
  endTime?: string;
}

/**
 * 日志视图对象类型
 */
export interface LogVO {
  /**
   * ID
   */
  id?: number;

  /**
   * 操作人
   */
  username?: string;

  /**
   * 用户ID
   */
  userId?: number;

  /**
   * IP地址
   */
  ip?: string;

  /**
   * 操作系统
   */
  os?: string;

  /**
   * 浏览器
   */
  browser?: string;

  /**
   * 模块名
   */
  module?: string;

  /**
   * 操作名
   */
  operation?: string;

  /**
   * 方法名
   */
  method?: string;

  /**
   * 请求URL
   */
  url?: string;

  /**
   * 请求参数
   */
  params?: string;

  /**
   * 结果 SUCCESS/FAIL
   */
  result?: string;

  /**
   * 错误信息
   */
  errorMsg?: string;

  /**
   * 创建时间
   */
  createTime?: string;
}
