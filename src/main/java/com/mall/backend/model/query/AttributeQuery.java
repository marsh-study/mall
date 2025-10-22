package com.mall.backend.model.query;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttributeQuery {

        @NotNull(message = "分类ID不能为空")
        private Long categoryId;

        // 1:规格;2:属性;
        @NotNull(message = "属性类型不能为空")
        private Integer type;

}
