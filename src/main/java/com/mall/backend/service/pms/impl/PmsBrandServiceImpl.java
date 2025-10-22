package com.mall.backend.service.pms.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.backend.model.entity.PmsBrand;
import com.mall.backend.mapper.pms.PmsBrandMapper;
import com.mall.backend.model.query.BrandQuery;
import com.mall.backend.model.vo.Brand;
import com.mall.backend.service.pms.PmsBrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.backend.util.PageResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品品牌表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
@Service
public class PmsBrandServiceImpl extends ServiceImpl<PmsBrandMapper, PmsBrand> implements PmsBrandService {

    @Override
    public List<Brand> getBrandList(BrandQuery brandQuery) {
        QueryWrapper queryWrapper =new QueryWrapper<PmsBrand>()
                .like(brandQuery.getName() != null, "name", brandQuery.getName())
                .orderByAsc("sort");
        List<PmsBrand> list = this.list(queryWrapper);
        List<Brand> brandList = list.stream().map(item -> {
            Brand brand = new Brand();
            BeanUtil.copyProperties(item, brand);
            return brand;
        }).collect(Collectors.toList());

        return brandList;
    }

    @Override
    public PageResult<Brand> getBrandListPage(BrandQuery brandQuery) {
        Page<PmsBrand> page = new Page<>(brandQuery.getPageNum(), brandQuery.getPageSize());
        QueryWrapper queryWrapper =new QueryWrapper<PmsBrand>()
                .like(brandQuery.getName() != null, "name", brandQuery.getName())
                .orderByAsc("sort");
        Page<PmsBrand> pageResult = this.page(page, queryWrapper);
        List<Brand> brandList = pageResult.getRecords().stream().map(item -> {
            Brand brand = new Brand();
            BeanUtil.copyProperties(item, brand);
            return brand;
        }).collect(Collectors.toList());
        return new PageResult<>(brandList, pageResult.getTotal());
    }


}
