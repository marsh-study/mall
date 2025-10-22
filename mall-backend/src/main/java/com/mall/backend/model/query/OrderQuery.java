package com.mall.backend.model.query;

import com.mall.backend.common.base.BasePageQuery;
import lombok.Data;

@Data
public class OrderQuery extends BasePageQuery {
    private String orderSn;
    private Integer status;
}
