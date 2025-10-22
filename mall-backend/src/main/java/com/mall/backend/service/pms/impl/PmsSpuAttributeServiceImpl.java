package com.mall.backend.service.pms.impl;

import com.mall.backend.model.entity.PmsSpuAttribute;
import com.mall.backend.mapper.pms.PmsSpuAttributeMapper;
import com.mall.backend.service.pms.PmsSpuAttributeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 商品属性/规格表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
@Service
public class PmsSpuAttributeServiceImpl extends ServiceImpl<PmsSpuAttributeMapper, PmsSpuAttribute> implements PmsSpuAttributeService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAttribute(List<PmsSpuAttribute> pmsSpuAttributes) {
        this.saveBatch(pmsSpuAttributes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAttribute(List<PmsSpuAttribute> pmsSpuAttributes) {
        this.updateBatchById(pmsSpuAttributes);
    }
}
