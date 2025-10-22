package com.mall.backend.model.query;

import com.mall.backend.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MenuQuery{
    /**
     * 菜单名称或描述搜索关键词
     */
    @Schema(description = "菜单名称")
    private String keywords;
}
