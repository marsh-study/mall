import request from "@/utils/request";
import { AxiosPromise } from "axios";
import { LogQuery, LogVO } from "./types";

/**
 * 获取日志分页列表
 *
 * @param queryParams
 */
export function getLogPage(
  queryParams: LogQuery
): AxiosPromise<PageResult<LogVO[]>> {
  // 处理时间范围参数，避免数组序列化问题
  const params = { ...queryParams };
  if (params.createTime && Array.isArray(params.createTime)) {
    // 将数组转换为独立的开始和结束时间参数
    params.startTime = params.createTime[0];
    params.endTime = params.createTime[1];
    delete params.createTime;
  }

  return request({
    url: "/api/logs",
    method: "get",
    params,
  });
}
