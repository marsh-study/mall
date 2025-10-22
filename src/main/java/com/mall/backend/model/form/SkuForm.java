package com.mall.backend.model.form;

import com.mall.backend.model.entity.PmsSku;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class SkuForm {

    private Long id;
    private Long spuId;
    @NotBlank(message = "商品编号不能为空")
    private String skuSn; // 商品编号

    private String name; // SKU名称
    private String specIds; // 规格ID组合，如"tid_1_1|tid_2_1"
    private String specValues; // 规格值组合，如"1_2_"
    private String specValue1; // 第一个规格的值
    private String specValue2; // 第二个规格的值（如果有）
    private String specValue3; // 第三个规格的值（如果有）

    @NotNull(message = "SKU价格不能为空")
    private Integer price; // SKU价格（单位：分）

    @NotBlank(message = "SKU库存不能为空")
    private String stock; // SKU库存

    private String picUrl; // SKU对应的图片URL

    public static SkuForm fromEntity(PmsSku entity) {
        SkuForm form = new SkuForm();
        form.setId(entity.getId());
        form.setSpuId(entity.getSpuId());
        form.setSkuSn(entity.getSkuSn());
        form.setName(entity.getName());
        form.setSpecIds(entity.getSpecIds());
        form.setPrice(entity.getPrice().intValue());
        form.setStock(String.valueOf(entity.getStock()));
        form.setPicUrl(entity.getPicUrl());
        return form;
    }

    public PmsSku toEntity() {
        PmsSku entity = new PmsSku();
        entity.setSpuId(spuId);
        entity.setSkuSn(skuSn);
        entity.setName(name);
        entity.setSpecIds(specIds);
        entity.setPrice(Long.valueOf(price));
        entity.setStock(Integer.valueOf(stock));
        entity.setPicUrl(picUrl);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        return entity;
    }

    public PmsSku toEntity(Date date) {
        PmsSku entity = new PmsSku();
        entity.setId(id);
        entity.setSpuId(spuId);
        entity.setSkuSn(skuSn);
        entity.setName(name);
        entity.setSpecIds(specIds);
        entity.setPrice(Long.valueOf(price));
        entity.setStock(Integer.valueOf(stock));
        entity.setPicUrl(picUrl);
        entity.setUpdateTime(date);
        return entity;
    }
}
