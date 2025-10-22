package com.mall.backend.service.pms;

import com.mall.backend.common.model.OptionType;
import com.mall.backend.model.entity.PmsCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.backend.model.vo.CategoryVO;
import com.mall.backend.util.Result;

import java.util.List;

/**
 * <p>
 * 商品分类表 服务类
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
public interface PmsCategoryService extends IService<PmsCategory> {

    List<OptionType> getCategoryOptions();

    List<CategoryVO> getCategoryTree(List<PmsCategory> list);
}
