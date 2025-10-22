package com.mall.backend.service.component;

import com.mall.backend.config.RabbitLogConfig;
import com.mall.backend.model.entity.SysLog;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送系统操作日志
     */
    public void sendSysLog(SysLog log) {
        try {
            rabbitTemplate.convertAndSend(RabbitLogConfig.LOG_EXCHANGE, "log.sys", log);
        } catch (Exception e) {
            // 记录到本地文件或告警
            System.err.println("发送系统日志到MQ失败: " + e.getMessage());
        }
    }

    /**
     * 发送订单日志
     */
//    public void sendOrderLog(OrderLog log) {
//        try {
//            rabbitTemplate.convertAndSend(RabbitLogConfig.LOG_EXCHANGE, "log.order", log);
//        } catch (Exception e) {
//            System.err.println("发送订单日志到MQ失败: " + e.getMessage());
//        }
//    }
}
