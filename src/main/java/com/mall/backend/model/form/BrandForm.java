package com.mall.backend.model.form;

import com.mall.backend.model.entity.PmsBrand;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class BrandForm {
    private Long id;
    @NotBlank(message = "品牌名称不能为空")
    private String name;
    private String logoUrl;
    private Integer sort;

    public PmsBrand convertToEntity() {
        PmsBrand pmsBrand = new PmsBrand();
        pmsBrand.setId(id);
        pmsBrand.setName(name);
        pmsBrand.setLogoUrl(logoUrl);
        pmsBrand.setSort(sort);
        pmsBrand.setCreateTime(new Date());
        pmsBrand.setUpdateTime(new Date());
        return pmsBrand;
    }

    public PmsBrand convertToEntity(Date updateTime) {
        PmsBrand pmsBrand = new PmsBrand();
        pmsBrand.setId(id);
        pmsBrand.setName(name);
        pmsBrand.setLogoUrl(logoUrl);
        pmsBrand.setSort(sort);
        pmsBrand.setUpdateTime(updateTime);
        return pmsBrand;
    }

    public static BrandForm convertFromEntity(PmsBrand pmsBrand) {
        BrandForm brandForm = new BrandForm();
        brandForm.setId(pmsBrand.getId());
        brandForm.setName(pmsBrand.getName());
        brandForm.setLogoUrl(pmsBrand.getLogoUrl());
        brandForm.setSort(pmsBrand.getSort());
        return brandForm;
    }
}
