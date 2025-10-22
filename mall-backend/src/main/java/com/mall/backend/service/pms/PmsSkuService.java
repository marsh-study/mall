package com.mall.backend.service.pms;

import com.mall.backend.model.entity.PmsSku;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品库存表 服务类
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
public interface PmsSkuService extends IService<PmsSku> {

    void addSku(List<PmsSku> pmsSkuList);

    void updateSku(List<PmsSku> pmsSkuList);
}
