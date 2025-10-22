package com.mall.backend.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Schema(description = "路由对象")
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteVO {

    @Schema(description = "路由路径", example = "user")
    private String path;

    @Schema(description = "组件路径", example = "system/user/index")
    private String component;

    @Schema(description = "跳转链接", example = "https://www.youlai.tech")
    private String redirect;

    @Schema(description = "路由名称")
    private String name;

    @Schema(description = "路由属性")
    private Meta meta;

    @Schema(description = "路由属性类型")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Meta {

        @Schema(description = "路由title")
        private String title;

        @Schema(description = "ICON")
        private String icon;

        @Schema(description = "是否隐藏(true-是 false-否)", example = "true")
        private Boolean hidden;

        @Schema(description = "【菜单】是否开启页面缓存", example = "true")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Boolean keepAlive;

        @Schema(description = "【目录】只有一个子路由是否始终显示", example = "true")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Boolean alwaysShow;

        @Schema(description = "路由参数")
        private Object params;

    }

    @Schema(description = "子路由列表")
    private List<RouteVO> children;
}
