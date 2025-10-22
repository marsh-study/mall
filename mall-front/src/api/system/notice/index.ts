import request from "@/utils/request";
import { AxiosPromise } from "axios";
import { NoticeForm, NoticePageVO, NoticeQuery } from "./types";

/**
 * 获取通知公告分页列表
 *
 * @param queryParams
 */
export function getNoticePage(
  queryParams: NoticeQuery
): AxiosPromise<PageResult<NoticePageVO[]>> {
  return request({
    url: "/api/notices/page",
    method: "get",
    params: queryParams,
  });
}

/**
 * 获取我的通知公告分页列表
 *
 * @param queryParams
 */
export function getMyNoticePage(
  queryParams: NoticeQuery
): AxiosPromise<PageResult<NoticePageVO[]>> {
  return request({
    url: "/api/notices/my",
    method: "get",
    params: queryParams,
  });
}

/**
 * 获取通知公告详情
 *
 * @param noticeId
 */
export function getNoticeForm(noticeId: number): AxiosPromise<NoticeForm> {
  return request({
    url: "/api/notices/" + noticeId + "/form",
    method: "get",
  });
}

/**
 * 获取通知公告详情（用于查看）
 *
 * @param id
 */
export function getDetail(id: string): AxiosPromise<any> {
  return request({
    url: "/api/notices/" + id,
    method: "get",
  });
}

/**
 * 添加通知公告
 *
 * @param data
 */
export function addNotice(data: NoticeForm) {
  return request({
    url: "/api/notices",
    method: "post",
    data: data,
  });
}

/**
 * 修改通知公告
 *
 * @param id
 * @param data
 */
export function updateNotice(id: number, data: NoticeForm) {
  return request({
    url: "/api/notices/" + id,
    method: "put",
    data: data,
  });
}

/**
 * 发布通知公告
 *
 * @param noticeId
 */
export function publishNotice(noticeId: number) {
  return request({
    url: "/api/notices/" + noticeId + "/publish",
    method: "patch",
  });
}

/**
 * 撤回通知公告
 *
 * @param noticeId
 */
export function revokeNotice(noticeId: number) {
  return request({
    url: "/api/notices/" + noticeId + "/revoke",
    method: "patch",
  });
}

/**
 * 删除通知公告
 *
 * @param ids
 */
export function deleteNotices(ids: string) {
  return request({
    url: "/api/notices/" + ids,
    method: "delete",
  });
}
