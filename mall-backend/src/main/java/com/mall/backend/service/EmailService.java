package com.mall.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class EmailService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void sendVerificationCode(String email) {
        // 检查频率限制
        if (canSendCode(email)) {
            // 将邮件任务加入队列
            String task = email + ":" + System.currentTimeMillis();
            redisTemplate.opsForList().leftPush("email_queue", task);
        }
    }

    private boolean canSendCode(String email) {
        String key = "email_limit:" + email;
        String count = redisTemplate.opsForValue().get(key);
        if (count == null) {
            redisTemplate.opsForValue().set(key, "1", 60, TimeUnit.SECONDS);
            return true;
        }
        int currentCount = Integer.parseInt(count);
        if (currentCount >= 3) {
            throw new RuntimeException("发送邮件过于频繁");
        }
        redisTemplate.opsForValue().increment(key, 1);
        return true;
    }

    public boolean validate(String email, String code) {

        String key = "email_code:" + email;
        String storedCode = redisTemplate.opsForValue().get(key);
        return storedCode != null && storedCode.equals(code);
    }
}
