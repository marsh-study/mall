package com.mall.backend.model.query;

import com.mall.backend.common.base.BasePageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictQuery extends BasePageQuery {
    private String keywords;

    private String typeCode;
}
