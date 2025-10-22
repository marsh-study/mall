package com.mall.backend.util;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.INameConvert;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/youlai?useSSL=false&serverTimezone=Asia/Shanghai", "root", "123456")
            .globalConfig(builder ->
                    builder.author("lsy")
                            .outputDir("D:/Develop/SpringBoot/mall-backend/src/main/java"))
                // 包路径配置（基于parent包名自动拼接）
                .packageConfig(builder ->
                        builder.parent("com.mall.backend") // 父包名
                                .entity("model.entity")        // 实体类包名 → com.mall.backend.model.entity
                                .mapper("mapper.oms")              // Mapper接口包名 → com.mall.backend.mapper
                                .service("service.oms")            // Service接口包名 → com.mall.backend.service.system
                                .serviceImpl("service.oms.impl") // Service实现类包名 → com.mall.backend.service.system.impl
                                .controller("controller")      // Controller包名 → com.mall.backend.controller
                                .pathInfo(Collections.singletonMap(
                                        OutputFile.xml,            // XML文件路径
                                        "D:/Develop/SpringBoot/mall-backend/src/main/resources/mapper"))
                )
                .templateEngine(new FreemarkerTemplateEngine())
                .templateConfig(builder ->
                        builder.disable(TemplateType.ENTITY)
                                .entity("/templates/entity.java")
                                .service("/templates/service.java")
                                .serviceImpl("/templates/serviceImpl.java")
                                .mapper("/templates/mapper.java")
                                .controller("/templates/controller.java"))
                .strategyConfig(builder ->
                builder.addInclude(new String[]{"oms_order_item"})
                    .entityBuilder().enableLombok()
                    .controllerBuilder().enableRestStyle()
                    .serviceBuilder().formatServiceFileName("%sService"))
            .execute();
    }
}
