package com.mall.backend.controller;

import com.mall.backend.annotation.SystemLog;
import com.mall.backend.model.entity.PmsBrand;
import com.mall.backend.model.form.BrandForm;
import com.mall.backend.model.query.BrandQuery;
import com.mall.backend.model.vo.Brand;
import com.mall.backend.service.pms.PmsBrandService;
import com.mall.backend.util.PageResult;
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
 * 商品品牌表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
@RestController
@RequestMapping("/brands")
public class PmsBrandController {

    @Autowired
    private PmsBrandService brandService;

    @GetMapping
    public Result<List<Brand>> getBrandList( BrandQuery brandQuery) {
        if (brandQuery==null){
            brandQuery=new BrandQuery();
        }
        List<Brand> brandList=brandService.getBrandList(brandQuery);
        return Result.success(brandList);

    }
/**
 * 获取品牌列表分页
 * @param brandQuery 查询参数
 * @return 分页结果
 */
    @GetMapping("/page")
    public Result<PageResult<Brand>> getBrandListPage(BrandQuery brandQuery) {
        if (brandQuery==null){
            brandQuery=new BrandQuery();
        }
        PageResult<Brand> pageResult=brandService.getBrandListPage(brandQuery);
        return Result.success(pageResult);
    }

    /**
     * 新增品牌
     * @param brandForm 品牌表单
     * @return 新增结果
     */
    @PostMapping
    @SystemLog(module = "品牌", operation = "新增品牌")
    public Result<String> addBrand(@Validated  @RequestBody BrandForm brandForm) {
        PmsBrand pmsBrand=brandForm.convertToEntity();
        boolean result=brandService.save(pmsBrand);
        if (result){
            return Result.success("保存成功");
        }
        return Result.fail("保存失败");
    }

    /**
     * 获取品牌表单
     * @param id
     * @return BrandForm 表单
     */
    @GetMapping("/{id}")
    public Result<BrandForm> getBrandForm(@PathVariable Long id){
        PmsBrand pmsBrand=brandService.getById(id);
        BrandForm brandForm=BrandForm.convertFromEntity(pmsBrand);
        return Result.success(brandForm);
    }

    /**
     * 修改品牌
     * @param id 商品ID
     * @param brandForm 表单
     * @return 修改结果
     */
    @PutMapping("/{id}")
    @SystemLog(module = "品牌", operation = "修改品牌")
    public Result<String> updateBrand(@PathVariable Long id, @Validated @RequestBody BrandForm brandForm){
        brandForm.setId(id);
        PmsBrand pmsBrand=brandForm.convertToEntity(new Date());
        boolean result=brandService.updateById(pmsBrand);
        if (result){
            return Result.success("修改成功");
        }
        return Result.fail("修改失败");
    }


    /**
     * 删除品牌
     * @param ids 商品ID列表，多个用逗号分隔
     * @return 删除结果
     */
    @DeleteMapping("/{ids}")
    @SystemLog(module = "品牌", operation = "删除品牌")
    public Result<String> delete(@PathVariable String ids){
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        boolean result=brandService.removeByIds(idList);
        if (result){
            return Result.success("删除成功");
        }
        return Result.fail("删除失败");
    }
}
