import request from "@/utils/request";
import { AxiosPromise } from "axios";
import {
  MemberQuery,
  MemberPageResult,
  MemberForm,
  AddressForm,
} from "./types";

/**
 * 获取会员分页列表
 *
 * @param queryParams
 */
export function getMemberPage(
  queryParams: MemberQuery
): AxiosPromise<MemberPageResult> {
  return request({
    url: "/api/members",
    method: "get",
    params: queryParams,
  });
}

/**
 * 获取会员详情
 *
 * @param id
 */
export function getMemberDetail(id: string) {
  return request({
    url: "/mall-ums/api/v1/members/" + id,
    method: "get",
  });
}

/**
 * 添加会员
 *
 * @param data
 */
export function addMember(data: MemberForm) {
  return request({
    url: "/mall-ums/api/v1/members",
    method: "post",
    data: data,
  });
}

/**
 * 修改会员
 *
 * @param id
 * @param data
 */
export function updateMember(id: string, data: MemberForm) {
  return request({
    url: "/mall-ums/api/v1/members/" + id,
    method: "put",
    data: data,
  });
}

/**
 * 删除会员
 *
 * @param ids
 */
export function deleteMembers(ids: string[]) {
  return request({
    url: "/mall-ums/api/v1/members",
    method: "delete",
    data: ids,
  });
}

/**
 * 获取会员地址列表
 *
 * @param memberId
 */
export function getMemberAddresses(memberId: string) {
  return request({
    url: `/mall-ums/api/v1/members/${memberId}/addresses`,
    method: "get",
  });
}

/**
 * 添加会员地址
 *
 * @param data
 */
export function addMemberAddress(data: AddressForm) {
  return request({
    url: "/mall-ums/api/v1/member-addresses",
    method: "post",
    data: data,
  });
}

/**
 * 修改会员地址
 *
 * @param id
 * @param data
 */
export function updateMemberAddress(id: string, data: AddressForm) {
  return request({
    url: "/mall-ums/api/v1/member-addresses/" + id,
    method: "put",
    data: data,
  });
}

/**
 * 删除会员地址
 *
 * @param ids
 */
export function deleteMemberAddresses(ids: string[]) {
  return request({
    url: "/mall-ums/api/v1/member-addresses",
    method: "delete",
    data: ids,
  });
}

/**
 * 设置默认地址
 *
 * @param memberId
 * @param addressId
 */
export function setDefaultAddress(memberId: string, addressId: string) {
  return request({
    url: `/mall-ums/api/v1/members/${memberId}/addresses/${addressId}/default`,
    method: "put",
  });
}
