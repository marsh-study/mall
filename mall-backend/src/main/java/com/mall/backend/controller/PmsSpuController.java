package com.mall.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mall.backend.annotation.SystemLog;
import com.mall.backend.model.entity.PmsSku;
import com.mall.backend.model.form.SpuForm;
import com.mall.backend.model.query.GoodsQuery;
import com.mall.backend.model.vo.Goods;
import com.mall.backend.service.pms.PmsBrandService;
import com.mall.backend.service.pms.PmsCategoryService;
import com.mall.backend.service.pms.PmsSkuService;
import com.mall.backend.service.pms.PmsSpuService;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
@RestController
@RequestMapping("/spu")
public class PmsSpuController {

    @Autowired
    private PmsSpuService spuService;

    @Autowired
    private PmsSkuService skuService;

    @Autowired
    private PmsCategoryService categoryService;

    @Autowired
    private PmsBrandService brandService;



    /**
     * 分页查询
     * @return PageResult<GoodsPageVO>
     */
    @GetMapping("/page")
    public Result<PageResult<Goods>> page(GoodsQuery query){
        PageResult<Goods> result = spuService.getPage(query);
        List<Goods> goodsList = result.getList().stream().peek(item -> {
            item.setCategoryName(categoryService.getById(item.getCategoryId()).getName());
            item.setBrandName(brandService.getById(item.getBrandId()).getName());
            item.setSkuList(skuService.list(new QueryWrapper<PmsSku>().eq("spu_id", item.getId())));
        }).collect(Collectors.toList());

        result.setList(goodsList);
        return Result.success(result);
    }

    /**
     * 添加
     * @param spuForm 商品表单数据
     * @return Result<String>
     */
    @PostMapping
    @SystemLog(module = "商品管理", operation = "添加商品")
    public Result<String> add(@Validated @RequestBody SpuForm spuForm){
        boolean save =spuService.addSpuWithRelatedData(spuForm);
        return save ? Result.success("添加成功") : Result.fail("添加失败");
    }

    /**
     * 详情
     * @param id 商品ID
     * @return SpuForm
     */
    @GetMapping("/{id}/detail")
    public Result<SpuForm> detail(@PathVariable Long id) throws JsonProcessingException {
        if (id == null){
            return Result.fail("参数错误");
        }
        SpuForm spuForm = spuService.getDetail( id);
        return Result.success(spuForm);
    }

    /**
     * 修改
     * @param spuForm 商品表单数据
     * @param id 商品ID
     * @return Result<String>
     */
    @PutMapping("/{id}")
    public Result<String> update( @PathVariable Long id,
                                 @Validated @RequestBody SpuForm spuForm){
        spuForm.setId(id);
        boolean update = spuService.updateSpuWithRelatedData(spuForm);
        return update ? Result.success("更新成功") : Result.fail("更新失败");
    }

    /**
     * 删除
     * @param ids 商品ID列表，多个用逗号分隔
     * @return Result<String>
     */
    @DeleteMapping("/{ids}")
    public Result<String> delete(@PathVariable String ids){
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        boolean delete = spuService.removeSpuByIds(idList);
        return delete ? Result.success("删除成功") : Result.fail("删除失败");
    }
}
