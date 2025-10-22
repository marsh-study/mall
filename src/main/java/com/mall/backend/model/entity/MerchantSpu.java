package com.mall.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author lsy
 * @since 2025-10-20
 */
@Getter
@Setter
@TableName("merchant_spu")
@AllArgsConstructor
@NoArgsConstructor
public class MerchantSpu implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer merchantId;

    private Long spuId;
}
