package com.mall.backend.model.query;

import com.mall.backend.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RoleQuery extends BasePageQuery {
        /**
         * 角色名称或描述搜索关键词
         */
        @Schema(description = "角色名称")
        private String keywords;
}
