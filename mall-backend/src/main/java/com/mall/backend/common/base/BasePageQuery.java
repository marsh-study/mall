package com.mall.backend.common.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BasePageQuery {
        @Schema(description = "页码", example = "1")
        private Integer pageNum;
        @Schema(description = "每页数量", example = "10")
        private Integer pageSize;
}
