package com.mall.backend.service.pms;

import com.mall.backend.model.entity.PmsBrand;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.backend.model.query.BrandQuery;
import com.mall.backend.model.vo.Brand;
import com.mall.backend.util.PageResult;

import java.util.List;

/**
 * <p>
 * 商品品牌表 服务类
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
public interface PmsBrandService extends IService<PmsBrand> {

    List<Brand> getBrandList(BrandQuery brandQuery);

    PageResult<Brand> getBrandListPage(BrandQuery brandQuery);
}
