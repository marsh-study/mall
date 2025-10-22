package com.mall.backend.consumer;

import com.mall.backend.util.VerificationCodeUtil;
import io.lettuce.core.RedisCommandTimeoutException;
import io.lettuce.core.RedisException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class EmailQueueConsumer {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @PostConstruct
    public void startProcessing() {
        // 启动后台线程处理队列
        Thread consumerThread = new Thread(this::processEmailQueue);
        consumerThread.setDaemon(false); // 设置为非守护线程
        consumerThread.setName("email-queue-consumer");
        consumerThread.start();
    }

    private void processEmailQueue() {
        // 连续出错计数，用于错误处理
        int errorCount = 0;
        int maxErrorCount = 10; // 最大连续错误次数
        
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // 使用较短的超时时间
                ListOperations<String, String> listOps = redisTemplate.opsForList();
                String task = listOps.rightPop("email_queue", 5, TimeUnit.SECONDS);

                if (task != null) {
                    String[] parts = task.split(":");
                    String email = parts[0];
                    sendEmailCode(email);
                }
                
                // 成功处理后重置错误计数
                errorCount = 0;
            } catch (RedisCommandTimeoutException e) {
                // 超时是正常的，继续下一次循环
                continue;
            } catch (RedisSystemException e) {
                // Redis系统异常，可能是连接问题
                errorCount++;
                System.err.println("Redis系统异常: " + e.getMessage());
                handleRedisException(errorCount, maxErrorCount, e);
            } catch (RedisException e) {
                // 其他Redis异常
                errorCount++;
                System.err.println("Redis异常: " + e.getMessage());
                handleRedisException(errorCount, maxErrorCount, e);
            } catch (Exception e) {
                errorCount++;
                System.err.println("处理邮件队列时发生异常: " + e.getMessage());
                e.printStackTrace();
                
                // 如果连续出错次数过多，则暂停更长时间
                if (errorCount >= maxErrorCount) {
                    System.err.println("连续出错次数过多，暂停处理30秒");
                    try {
                        Thread.sleep(30000); // 暂停30秒
                        errorCount = 0; // 重置错误计数
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    try {
                        Thread.sleep(3000); // 出错后短暂休眠
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
    }

    /**
     * 处理Redis异常
     * @param errorCount 当前错误计数
     * @param maxErrorCount 最大错误计数
     * @param e 异常对象
     */
    private void handleRedisException(int errorCount, int maxErrorCount, Exception e) {
        if (errorCount >= maxErrorCount) {
            System.err.println("Redis连接异常次数过多，暂停处理30秒");
            try {
                Thread.sleep(30000); // 暂停30秒
                errorCount = 0; // 重置错误计数
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        } else {
            try {
                Thread.sleep(5000); // Redis异常后暂停5秒
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void sendEmailCode(String email) {
        try {
            // 生成验证码
            String code = VerificationCodeUtil.getVerificationCode(6);

            // 发送邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("验证码");
            message.setText("您的验证码是：" + code + "，5分钟内有效。");

            // 添加连接测试
            if (mailSender instanceof JavaMailSenderImpl) {
                JavaMailSenderImpl sender = (JavaMailSenderImpl) mailSender;
                System.out.println("SMTP Host: " + sender.getHost());
                System.out.println("SMTP Port: " + sender.getPort());
            }

            mailSender.send(message);

            // 存储验证码到Redis
            redisTemplate.opsForValue().set("email_code:" + email, code, 300, TimeUnit.SECONDS);

        } catch (MailAuthenticationException e) {
            System.err.println("邮件认证失败，请检查用户名和授权码");
            e.printStackTrace();
        } catch (MailSendException e) {
            System.err.println("邮件发送失败：" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("邮件发送异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

}