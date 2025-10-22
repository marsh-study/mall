package com.mall.backend.controller;

import com.mall.backend.annotation.SystemLog;
import com.mall.backend.model.dto.CategoryFormData;
import com.mall.backend.model.query.AttributeQuery;
import com.mall.backend.model.vo.AttributeVO;
import com.mall.backend.service.pms.PmsCategoryAttributeService;
import com.mall.backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品属性表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
@RestController
@RequestMapping("/attributes")
public class PmsCategoryAttributeController {

    @Autowired
    private PmsCategoryAttributeService attributeService;

    @GetMapping
    public Result<List<AttributeVO>> getAttributeList( @Validated AttributeQuery queryParams) {

        List<AttributeVO> attributeList = attributeService.getAttributeList(queryParams);
        return Result.success(attributeList);
    }

    /**
     *批量修改商品属性
     * @param categoryFormData 商品属性表单数据
     */
    @PostMapping("/batch")
    @SystemLog(module = "商品属性", operation = "批量修改商品属性")
    public Result<String> saveAttributeBatch(@Validated @RequestBody CategoryFormData categoryFormData) {
        boolean result =attributeService.saveAttributeBatch(categoryFormData);
        return result ? Result.success("保存成功") : Result.fail("保存失败");
    }

}
