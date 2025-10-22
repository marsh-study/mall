package com.mall.backend.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mall.backend.enums.ResultCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Data
public class Result<T> {
    private static final Logger log = LoggerFactory.getLogger(Result.class);
    
    private String code;
    private String msg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    private Result() {}

    private Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功，无数据
     */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg());
    }

    /**
     * 成功，带数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }

    /**
     * 成功，自定义消息
     */
    public static <T> Result<T> success(String msg) {
        return new Result<>("00000", msg);
    }

    /**
     * 成功，自定义消息 + 数据
     */
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>("00000", msg, data);
    }

    /**
     * 失败：使用枚举
     */
    public static <T> Result<T> fail(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMsg());
    }

    /**
     * 失败：自定义消息
     */
    public static <T> Result<T> fail(String msg) {
        return new Result<>("A0001", msg);
    }

    /**
     * 失败：自定义 code + msg
     */
    public static <T> Result<T> fail(String code, String msg) {
        return new Result<>(code, msg);
    }


    public static void out(HttpServletResponse response, Result result) {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSON.toJSONString(result));
            writer.flush(); // 强制刷新
        } catch (IOException e) {
            // 记录日志，避免异常传播中断响应
            log.error("写入响应流出错", e);
        }
    }

    /**
     * 文件导出响应封装（用于返回二进制文件流）
     * @param fileBytes 文件字节数组（Excel/CSV 等）
     * @param fileName 下载文件名（含扩展名）
     * @return ResponseEntity<byte[]> 二进制流响应
     */
    public static ResponseEntity<byte[]> exportFile(byte[] fileBytes, String fileName) {
        HttpHeaders headers = new HttpHeaders();
        // 设置二进制流类型（对应前端 arraybuffer）
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        
        // 使用标准方式设置文件名，更好地支持中文和特殊字符
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        headers.add("Content-Disposition", "attachment; filename*=" + encodedFileName);

        // 返回二进制流响应
        return ResponseEntity.ok()
                .headers(headers)
                .body(fileBytes);
    }

    public static ResponseEntity<byte[]> error(String s) {
        return ResponseEntity.badRequest().body(s.getBytes());
    }
}