package com.mall.backend.model.query;

import com.mall.backend.common.base.BasePageQuery;
import lombok.Data;

@Data
public class LogQuery extends BasePageQuery {

    /**
     * 模块名
     */
    private String module;

    /**
     * 操作人
     */
    private String username;

    /**
     * 起始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
