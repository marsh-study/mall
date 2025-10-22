package com.mall.backend.service.pms.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.backend.mapper.pms.PmsCategoryAttributeMapper;
import com.mall.backend.model.dto.CategoryFormData;
import com.mall.backend.model.entity.PmsCategoryAttribute;
import com.mall.backend.model.query.AttributeQuery;
import com.mall.backend.model.vo.AttributeVO;
import com.mall.backend.service.pms.PmsCategoryAttributeService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * <p>
 * 商品属性表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
@Service
public class PmsCategoryAttributeServiceImpl extends ServiceImpl<PmsCategoryAttributeMapper, PmsCategoryAttribute> implements PmsCategoryAttributeService {

    @Override
    public List<AttributeVO> getAttributeList(AttributeQuery queryParams) {
        QueryWrapper<PmsCategoryAttribute> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(queryParams.getCategoryId() != null, "category_id", queryParams.getCategoryId())
                .eq(queryParams.getType() != null, "type", queryParams.getType());
        List<PmsCategoryAttribute> list = this.list(queryWrapper);
        return list.stream().map(item -> {
            AttributeVO attributeVO = new AttributeVO();
            BeanUtil.copyProperties(item, attributeVO);
            return attributeVO;
        }).toList();
    }

    @Override
    public boolean saveAttributeBatch(CategoryFormData categoryFormData) {
        List<PmsCategoryAttribute> pmsCategoryAttributeList = CategoryFormData.convertToEntity(categoryFormData);;
        if (pmsCategoryAttributeList.isEmpty()){
            QueryWrapper<PmsCategoryAttribute> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("category_id", categoryFormData.getCategoryId())
                    .eq("type", categoryFormData.getType());
            return this.remove(queryWrapper);
        }
        return this.saveOrUpdateBatch(pmsCategoryAttributeList);
    }


}
