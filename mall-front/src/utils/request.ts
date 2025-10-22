import axios, { InternalAxiosRequestConfig, AxiosResponse } from "axios";
import { useUserStoreHook } from "@/store/modules/user";
import { logoutApi, refreshTokenApi } from "@/api/auth";
import { c } from "vite/dist/node/types.d-aGj9QkWt";

// 创建 axios 实例
const service = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  timeout: 50000,
  headers: { "Content-Type": "application/json;charset=utf-8" },
});

// 正在刷新token的标识
let isRefreshing = false;
// 重试队列，每一项将包含待执行的promise和原始请求配置
let requests: any[] = [];

let tokenType = "Bearer ";
// 请求拦截器
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const accessToken = localStorage.getItem("accessToken");

    if (accessToken) {
      config.headers.Authorization = accessToken;
    }
    return config;
  },
  (error: any) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const { code, msg } = response.data;
    if (code === "00000") {
      return response.data;
    }
    // 响应数据为二进制流处理(Excel导出)
    if (response.data instanceof ArrayBuffer) {
      return response;
    }

    // token 过期,重新登录
    if (code === "A0230") {
      // 创建一个Promise对象用于存储当前请求的解决函数
      const originalRequest = response.config;

      // 如果当前不在刷新token的状态
      if (!isRefreshing) {
        isRefreshing = true;

        const refreshToken = localStorage.getItem("refreshToken");
        if (refreshToken) {
          // 尝试刷新token
          refreshTokenApi(refreshToken)
            .then(({ data }) => {
              tokenType = data.tokenType ?? "Bearer ";
              const { accessToken, refreshToken: newRefreshToken } = data;
              localStorage.setItem(
                "accessToken",
                tokenType + " " + accessToken
              );
              if (newRefreshToken) {
                localStorage.setItem(
                  "refreshToken",
                  tokenType + " " + newRefreshToken
                );
              }

              // 重新执行队列中的请求
              requests.forEach((cb) => cb(accessToken));
              requests = [];
            })
            .catch(() => {
              // 刷新失败，需要重新登录
              handleTokenRefreshFailure();
            })
            .finally(() => {
              isRefreshing = false;
            });
        } else {
          // 没有refreshToken，需要重新登录
          handleTokenRefreshFailure();
        }
      }

      // 返回一个Promise，将当前请求加入队列，等待token刷新后重试
      return new Promise((resolve) => {
        // 将当前请求添加到等待队列
        requests.push((token: string) => {
          // 给当前请求设置新的token
          originalRequest.headers.Authorization = tokenType + " " + token;
          // 重新发送请求并解决Promise
          resolve(service(originalRequest));
        });
      });
    } else if (
      code === "401" ||
      code === "A0231" ||
      code === "A0232" ||
      code === "A0234"
    ) {
      // ElMessage.error(msg || "请重新登录");
      ElMessageBox.alert(msg || "请重新登录", "提示", {
        confirmButtonText: "确定",
        callback: () => {
          const userStore = useUserStoreHook();
          userStore.resetToken().then(() => {
            location.reload();
          });
        },
      });
      return Promise.reject(new Error(msg || "请重新登录"));
    }

    ElMessage.error(msg || "系统出错");
    return Promise.reject(new Error(msg || "Error"));
  },
  (error: any) => {
    if (error.response && error.response.data) {
      const { code, msg } = error.response.data;
      if (
        code === "401" ||
        code === "A0231" ||
        code === "A0232" ||
        code === "A0234"
      ) {
        // ElMessage.error(msg || "请重新登录");
        ElMessageBox.alert(msg || "请重新登录", "提示", {
          confirmButtonText: "确定",
          type: "warning",
        });
      } else {
        ElMessage.error(msg || "系统出错");
      }
    } else {
      ElMessage.error("网络错误");
    }
    return Promise.reject(error.message);
  }
);

// 处理token刷新失败的情况
function handleTokenRefreshFailure() {
  ElMessageBox.confirm("登录已过期，请重新登录", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    callback: () => {
      const userStore = useUserStoreHook();
      userStore.resetToken().then(() => {
        location.reload();
      });
    },
  }).then(() => {
    const userStore = useUserStoreHook();
    userStore.resetToken().then(() => {
      location.reload();
    });
  });
}

// 导出 axios 实例
export default service;
