/**
 * 文件处理工具函数
 */

import request from "./request";

/**
 * 处理文件URL，将本地路径转换为可访问的URL
 * @param url 文件路径
 * @returns 可访问的文件URL
 */
export function getImageUrl(url: string): string {
  // 如果是完整的URL (http:// 或 https:// 开头)，直接返回
  if (url && (url.startsWith("http://") || url.startsWith("https://"))) {
    return url;
  }

  // 如果是相对路径（以 / 开头）
  if (url && url.startsWith("/")) {
    // 获取环境变量
    const baseUrl = import.meta.env.VITE_APP_API_URL;

    // 如果环境变量未定义，则使用默认值
    const apiUrl = baseUrl || "http://localhost:9999";

    // 如果是以 /dev-api 开头，则替换为实际的 API 地址
    if (url.startsWith(import.meta.env.VITE_APP_BASE_API)) {
      // 例如: /dev-api/api/files/xxx.jpg -> http://localhost:9999/api/files/xxx.jpg
      return `${apiUrl}${url.substring(import.meta.env.VITE_APP_BASE_API.length)}`;
    } else {
      // 其他以 / 开头的路径，直接添加基础API地址
      // 例如: /api/files/xxx.jpg -> http://localhost:9999/api/files/xxx.jpg
      return `${apiUrl}${url}`;
    }
  }

  // 其他情况返回原URL
  return url;
}
