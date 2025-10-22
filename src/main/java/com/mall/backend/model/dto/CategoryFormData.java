package com.mall.backend.model.dto;

import com.mall.backend.model.entity.PmsCategoryAttribute;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
public class CategoryFormData {
    @NotNull
    private Long categoryId;
    @NotNull
    private Integer type;
    private List<Attribute> attributes = new ArrayList<>();

    @Data
    public static class Attribute {
        private Long id;
        @NotBlank
        private String name;
    }

    public static List<PmsCategoryAttribute> convertToEntity(CategoryFormData categoryFormData){
        List<PmsCategoryAttribute> list = categoryFormData.getAttributes().stream().map(attribute -> {
            if (attribute.getName()== null||attribute.getName().isEmpty()) {
                return null;
            }
            PmsCategoryAttribute pmsCategoryAttribute = new PmsCategoryAttribute();
            pmsCategoryAttribute.setId(attribute.getId());
            pmsCategoryAttribute.setCategoryId(categoryFormData.getCategoryId());
            pmsCategoryAttribute.setName(attribute.getName());
            pmsCategoryAttribute.setType(categoryFormData.getType());
            pmsCategoryAttribute.setCreateTime(new Date());
            pmsCategoryAttribute.setUpdateTime(new Date());
            return pmsCategoryAttribute;
        })
                .filter(Objects::nonNull)
                .toList();
        return list;
    }

}
