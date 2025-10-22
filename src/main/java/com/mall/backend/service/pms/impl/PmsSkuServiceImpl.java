package com.mall.backend.service.pms.impl;

import com.mall.backend.model.entity.PmsSku;
import com.mall.backend.mapper.pms.PmsSkuMapper;
import com.mall.backend.service.pms.PmsSkuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 商品库存表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
@Service
public class PmsSkuServiceImpl extends ServiceImpl<PmsSkuMapper, PmsSku> implements PmsSkuService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSku(List<PmsSku> pmsSkuList) {
        this.saveBatch(pmsSkuList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSku(List<PmsSku> pmsSkuList) {
        this.updateBatchById(pmsSkuList);
    }
}
