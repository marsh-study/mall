package com.mall.backend.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitLogConfig {

    public static final String LOG_QUEUE = "sys.log.queue";
    public static final String ORDER_LOG_QUEUE = "order.log.queue";
    public static final String LOG_EXCHANGE = "sys.log.exchange";

    @Bean
    public Queue sysLogQueue() {
        return QueueBuilder.durable(LOG_QUEUE).build();
    }

    @Bean
    public Queue orderLogQueue() {
        return QueueBuilder.durable(ORDER_LOG_QUEUE).build();
    }

    @Bean
    public DirectExchange logExchange() {
        return new DirectExchange(LOG_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingSysLog() {
        return BindingBuilder.bind(sysLogQueue())
                .to(logExchange())
                .with("log.sys");
    }

    @Bean
    public Binding bindingOrderLog() {
        return BindingBuilder.bind(orderLogQueue())
                .to(logExchange())
                .with("log.order");
    }
}