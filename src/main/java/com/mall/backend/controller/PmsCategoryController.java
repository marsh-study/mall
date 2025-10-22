package com.mall.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall.backend.annotation.SystemLog;
import com.mall.backend.common.model.OptionType;
import com.mall.backend.model.entity.PmsCategory;
import com.mall.backend.model.form.CategoryForm;
import com.mall.backend.model.vo.CategoryVO;
import com.mall.backend.service.pms.PmsCategoryService;
import com.mall.backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品分类表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
@RestController
@RequestMapping("/categories")
public class PmsCategoryController {

    @Autowired
    private PmsCategoryService categoryService;

    @GetMapping("/options")
    public Result<List<OptionType>> getCategoryOptions(){
        List<OptionType> categoryOptions = categoryService.getCategoryOptions();
        return Result.success(categoryOptions);
    }

    /**
     * 获取商品分类列表
     * @return
     */
    @GetMapping
    public Result<List<CategoryVO>> list(){
        List<PmsCategory> list = categoryService.list(new QueryWrapper<PmsCategory>().orderByAsc("sort"));
        List<CategoryVO> categoryTree =categoryService.getCategoryTree(list);
        return Result.success(categoryTree);
    }

    /**
     * 保存商品分类
     * @param categoryForm 商品分类表单数据
     * @return
     */
    @PostMapping
    @SystemLog(module = "商品分类", operation = "保存商品分类")
    public Result<String> save(@Validated @RequestBody CategoryForm categoryForm ){
        boolean result = categoryService.save(categoryForm.convertToEntity());
        return result ? Result.success("保存成功") : Result.fail("保存失败");
    }

    /**
     * 修改商品分类
     * @param categoryForm 商品分类表单数据
     * @param id 商品分类ID
     * @return
     */
    @PutMapping("/{id}")
    @SystemLog(module = "商品分类", operation = "修改商品分类")
    public Result<String> update(@Validated @RequestBody CategoryForm categoryForm, @PathVariable Long id){
        categoryForm.setId(id);
        boolean result = categoryService.updateById(categoryForm.convertToEntity(new Date()));
        return result ? Result.success("修改成功") : Result.fail("修改失败");
    }

    /**
     * 删除商品分类
     * @param ids 商品分类ID列表，多个用逗号分隔
     * @return
     */
    @DeleteMapping("/{ids}")
    @SystemLog(module = "商品分类", operation = "删除商品分类")
    public Result<String> delete(@PathVariable String ids){
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        boolean result = categoryService.removeByIds(idList);
        return result ? Result.success("删除成功") : Result.fail("删除失败");
    }

}
