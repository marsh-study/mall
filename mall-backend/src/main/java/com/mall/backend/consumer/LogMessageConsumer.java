package com.mall.backend.consumer;

import com.mall.backend.config.RabbitLogConfig;
import com.mall.backend.model.entity.SysLog;
import com.mall.backend.service.system.SysLogService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogMessageConsumer {

    @Autowired
    private SysLogService logService;



    @RabbitListener(queues = RabbitLogConfig.LOG_QUEUE)
    public void consumeSysLog(SysLog log) {
        try {
            logService.save(log);
        } catch (Exception e) {
            // 可重试或存入死信队列
            System.err.println("消费系统日志失败: " + e.getMessage());
        }
    }

//    @RabbitListener(queues = RabbitLogConfig.ORDER_LOG_QUEUE)
//    public void consumeOrderLog(OrderLog log) {
//        try {
//            orderLogRepository.save(log);
//        } catch (Exception e) {
//            System.err.println("消费订单日志失败: " + e.getMessage());
//        }
//    }
}
