package com.mall.backend.service.pms;

import com.mall.backend.model.entity.PmsSpuAttribute;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品属性/规格表 服务类
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
public interface PmsSpuAttributeService extends IService<PmsSpuAttribute> {

    void addAttribute(List<PmsSpuAttribute> pmsSpuAttributes);

    void updateAttribute(List<PmsSpuAttribute> pmsSpuAttributes);
}
