package com.mall.backend.model.query;

import com.mall.backend.common.base.BasePageQuery;
import lombok.Data;

@Data
public class NoticeQuery extends BasePageQuery {

    private String title;
    private String type;
    private String publisher;
    private String level;
    private String status;
}
