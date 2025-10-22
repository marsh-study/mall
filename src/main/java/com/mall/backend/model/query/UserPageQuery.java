package com.mall.backend.model.query;

import com.mall.backend.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPageQuery extends BasePageQuery {
    @Schema(description = "搜索关键词", example = "用户名/手机号/昵称")
    private String keywords;
    @Schema(description = "用户状态", example = "1")
    private Integer status;
}
