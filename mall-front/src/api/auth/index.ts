import request from "@/utils/request";
import { AxiosPromise } from "axios";
import { CaptchaResult, LoginData, LoginResult } from "./types";

/**
 * 登录API
 *
 * @param data {LoginData}
 * @returns
 */
export function loginApi(data: LoginData): AxiosPromise<LoginResult> {
  // 默认 grant_type
  const payload = {
    ...data,
    grant_type: data.grant_type || "password",
  };

  return request({
    url: "/api/auth/login",
    method: "post",
    data: payload,
  });
}

/**
 * 刷新token
 *
 * @param refreshToken 刷新token
 * @returns
 */
export function refreshTokenApi(
  refreshToken: string
): AxiosPromise<LoginResult> {
  return request({
    url: "/api/auth/refresh",
    method: "post",
    data: {
      refreshToken: refreshToken,
    },
  });
}

/**
 * 获取验证码
 */
export function getCaptchaApi(): AxiosPromise<CaptchaResult> {
  return request({
    url: "/api/auth/captcha",
    method: "get",
  });
}

/**
 * 注销API
 */
export function logoutApi() {
  return request({
    url: "/api/auth/logout",
    method: "delete",
  });
}
