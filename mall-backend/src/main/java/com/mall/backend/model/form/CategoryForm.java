package com.mall.backend.model.form;

import com.mall.backend.model.entity.PmsCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CategoryForm {
    private Long id;
    @NotBlank(message = "分类名称不能为空")
    private String name;
    private Long parentId;
    private Integer level;
    private String iconUrl;
    private Integer visible;
    private Integer sort;

    private List<CategoryForm> children;

    public PmsCategory convertToEntity() {
        PmsCategory category = new PmsCategory();
        category.setId(id);
        category.setName(name);
        category.setParentId(parentId);
        category.setLevel(level);
        category.setIconUrl(iconUrl);
        category.setVisible(visible);
        category.setSort(sort);
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        return category;
    }

    public PmsCategory convertToEntity(Date updateTime) {
        PmsCategory category = new PmsCategory();
        category.setId(id);
        category.setName(name);
        category.setParentId(parentId);
        category.setLevel(level);
        category.setIconUrl(iconUrl);
        category.setVisible(visible);
        category.setSort(sort);
        category.setUpdateTime(updateTime);
        return category;
    }

}
