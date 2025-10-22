package com.mall.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
@Getter
@Setter
@TableName("pms_category_brand")
public class PmsCategoryBrand implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("category_id")
    private Long categoryId;

    private Long brandId;
}
