package com.mall.backend.model.query;

import com.mall.backend.common.base.BasePageQuery;
import lombok.Data;

@Data
public class DictTypeQuery extends BasePageQuery {
    private String keywords;
    private String typeCode;
}
