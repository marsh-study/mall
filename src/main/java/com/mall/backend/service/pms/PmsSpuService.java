package com.mall.backend.service.pms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mall.backend.model.entity.PmsSpu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.backend.model.form.SpuForm;
import com.mall.backend.model.query.GoodsQuery;
import com.mall.backend.model.vo.Goods;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
public interface PmsSpuService extends IService<PmsSpu> {

    PageResult<Goods> getPage(GoodsQuery query);


    boolean addSpuWithRelatedData(SpuForm spuForm);

    SpuForm getDetail(Long id) throws JsonProcessingException;

    boolean updateSpuWithRelatedData(SpuForm spuForm);

    boolean removeSpuByIds(List<Long> idList);
}
