package com.mall.backend.model.query;

import com.mall.backend.common.base.BasePageQuery;
import lombok.Data;

@Data
public class GoodsQuery extends BasePageQuery {
    private String name;
    private Long categoryId;
}
