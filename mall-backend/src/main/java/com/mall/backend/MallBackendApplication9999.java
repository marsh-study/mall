package com.mall.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@MapperScan("com.mall.backend.mapper")
public class MallBackendApplication9999 {

    public static void main(String[] args) {
        SpringApplication.run(MallBackendApplication9999.class, args);
    }
}
