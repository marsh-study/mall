package com.mall.backend.service.pms;

import com.mall.backend.model.dto.CategoryFormData;
import com.mall.backend.model.entity.PmsCategoryAttribute;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.backend.model.query.AttributeQuery;
import com.mall.backend.model.vo.AttributeVO;

import java.util.List;

/**
 * <p>
 * 商品属性表 服务类
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
public interface PmsCategoryAttributeService extends IService<PmsCategoryAttribute> {

    List<AttributeVO> getAttributeList(AttributeQuery queryParams);

    boolean saveAttributeBatch(CategoryFormData categoryFormData);
}
