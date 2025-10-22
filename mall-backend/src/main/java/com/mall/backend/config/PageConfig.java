package com.mall.backend.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PageConfig {
    @Bean
    public MybatisPlusInterceptor getInfo(){
        //创建MybatisPlusInterceptor对象
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //创建PaginationInnerInterceptor对象
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        //将PaginationInnerInterceptor对象添加到MybatisPlusInterceptor对象中
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        //返回MybatisPlusInterceptor对象
        return interceptor;
    }
}
