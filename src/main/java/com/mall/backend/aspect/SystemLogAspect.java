package com.mall.backend.aspect;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mall.backend.annotation.SystemLog;
import com.mall.backend.model.dto.LoginRequest;
import com.mall.backend.model.entity.SysLog;
import com.mall.backend.model.entity.SysUser;
import com.mall.backend.service.component.LogMessageProducer;
import com.mall.backend.service.system.impl.UserServiceImpl;
import com.mall.backend.util.IpUtils;
import com.mall.backend.util.UserAgentUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Aspect
@Component
public class SystemLogAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LogMessageProducer logMessageProducer;

    @Autowired
    private UserServiceImpl userService;

    @Pointcut("@annotation(com.mall.backend.annotation.SystemLog)")
    public void logPointCut() {}

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SystemLog systemLog = signature.getMethod().getAnnotation(SystemLog.class);

        SysLog log = new SysLog();
        log.setModule(systemLog.module());
        log.setOperation(systemLog.operation());
        log.setMethod(signature.getDeclaringTypeName() + "." + signature.getName());
        log.setUrl(request.getRequestURL().toString());
        log.setIp(IpUtils.getClientIpAddress(request));
        log.setBrowser(UserAgentUtils.parseBrowser(request.getHeader("User-Agent")));
        log.setOs(UserAgentUtils.parseOS(request.getHeader("User-Agent")));


        // 特殊处理登录接口 - 从参数中获取用户名
        String username = "anonymous";
        Long userId = null;

        if ("login".equals(signature.getName()) && joinPoint.getArgs().length > 0) {
            // 登录接口，从 LoginRequest 参数中获取用户名
            for (Object arg : joinPoint.getArgs()) {
                if (arg instanceof LoginRequest) {
                    username = ((LoginRequest) arg).getUsername();
                    // 根据用户名查询用户ID
                    userId=userService.getOne(new QueryWrapper<SysUser>().eq("username", username)).getId();
                    break;
                }
            }
        } else {
            // 正常流程，从安全上下文中获取
            username = userService.getCurrentUserName();
            userId = userService.getCurrentUserId();
            username = username != null ? username : "anonymous";
        }

        log.setUsername(username);
        log.setUserId(userId);

        // 参数脱敏 + 序列化
        ObjectMapper mapper = new ObjectMapper();
        // 配置ObjectMapper忽略无法序列化的对象
        mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 添加自定义序列化器处理MultipartFile
        SimpleModule module = new SimpleModule();
        module.addSerializer(MultipartFile.class, new MultipartFileSerializer());
        mapper.registerModule(module);
        
        String params = mapper.writeValueAsString(joinPoint.getArgs());
        // 密码字段完全隐藏
        params = params.replaceAll("\"password\":\"[^\"]*", "\"password\":\"****\"");
        // 邮箱用户名部分脱敏
        params = params.replaceAll("\"email\":\"([\\w\\.\\-]+)(@[^@]+)", "\"email\":\"$1****$2\"");

        params = params.replaceAll("\"mobile\":\"(\\d{3})\\d{4}", "\"mobile\":\"$1****\"");
//        params = params.replaceAll("\"idCard\":\"(\\d{6})\\d{8}", "\"idCard\":\"$1********\"");
        log.setParams(params);

        try {
            Object result = joinPoint.proceed();
            log.setResult("SUCCESS");
            return result;
        } catch (Exception e) {
            log.setResult("FAIL");
            log.setErrorMsg(ExceptionUtils.getStackTrace(e));
            throw e;
        } finally {
            log.setCreateTime(new Date());
            // ✅ 发送 MQ 消息
            logMessageProducer.sendSysLog(log);
        }
    }
}