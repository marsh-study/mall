package com.mall.backend.model.form;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.backend.model.entity.PmsSpu;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Data
public class SpuForm {
    // 商品基本信息
    private Long id; // 商品ID（更新时存在，新增时不存在）

    @NotBlank(message = "商品名称不能为空")
    private String name; // 商品名称

    @NotNull(message = "分类ID不能为空")
    private Long categoryId; // 分类ID

    @NotNull(message = "品牌ID不能为空")
    private Long brandId; // 品牌ID

    @NotNull(message = "原价不能为空")
    private Integer originPrice; // 原价（单位：分）

    @NotNull(message = "销售价格不能为空")
    private Integer price; // 销售价格（单位：分）

    private String description; // 商品描述
    private String detail; // 商品详情（HTML格式）
    private String picUrl; // 商品主图URL
    private List<String> subPicUrls; // 商品子图URL列表
    private List<String> album; // 商品相册（通常为空）

    // 关联数据列表
    private List<AttributeForm> attrList; // 商品属性列表
    private List<SpecForm> specList; // 商品规格列表
    private List<SkuForm> skuList; // 商品SKU列表

    public static SpuForm fromEntity(PmsSpu entity) throws JsonProcessingException {

        SpuForm form = new SpuForm();
        form.setId(entity.getId());
        form.setName(entity.getName());
        form.setCategoryId(entity.getCategoryId());
        form.setBrandId(entity.getBrandId());
        form.setOriginPrice(entity.getOriginPrice().intValue());
        form.setPrice(entity.getPrice().intValue());
        form.setDescription(entity.getDescription());
        form.setDetail(entity.getDetail());
        form.setPicUrl(entity.getPicUrl());
        //把json转换为List<String>
        List<String> list = entity.getAlbum() != null ? new ObjectMapper().readValue(entity.getAlbum(), List.class) : null;
        form.setAlbum(list);
        form.setSubPicUrls(list);
        form.setAttrList(entity.getAttributeList().stream().map(AttributeForm::fromEntity).collect(Collectors.toList()));
        form.setSpecList(entity.getSpecList().stream().map(SpecForm::fromEntity).collect(Collectors.toList()));

        form.setSkuList(entity.getSkuList().stream().map(SkuForm::fromEntity).collect(Collectors.toList()));
        return form;
    }

    public PmsSpu toPmsSpu() {
        PmsSpu spu = new PmsSpu();
        spu.setName( name);
        spu.setCategoryId(categoryId);
        spu.setBrandId(brandId);
        spu.setOriginPrice(Long.valueOf(originPrice));
        spu.setPrice(Long.valueOf(price));
        spu.setDescription(description);
        spu.setDetail(detail);
        spu.setPicUrl(picUrl);
        // 将 subPicUrls 转换为 JSON 格式
        if (this.subPicUrls != null && !this.subPicUrls.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String albumJson = objectMapper.writeValueAsString(this.subPicUrls);
                spu.setAlbum(albumJson);
            } catch (Exception e) {
                spu.setAlbum("[]"); // 出错时设置默认值
            }
        } else {
            spu.setAlbum("[]"); // 空列表时设置为空数组
        }
        spu.setStatus(1);
        spu.setCreateTime(new Date());
        spu.setUpdateTime(new Date());
        return spu;
    }

    public PmsSpu toPmsSpu(Date date) {
        PmsSpu spu = new PmsSpu();
        spu.setId(id);
        spu.setName(name);
        spu.setCategoryId(categoryId);
        spu.setBrandId(brandId);
        spu.setOriginPrice(Long.valueOf(originPrice));
        spu.setPrice(Long.valueOf(price));
        spu.setDescription(description);
        spu.setDetail(detail);
        spu.setPicUrl(picUrl);
        // 将 subPicUrls 转换为 JSON 格式
        if (this.subPicUrls != null && !this.subPicUrls.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String albumJson = objectMapper.writeValueAsString(this.subPicUrls);
                spu.setAlbum(albumJson);
            } catch (Exception e) {
                spu.setAlbum("[]"); // 出错时设置默认值
            }
        } else {
            spu.setAlbum("[]"); // 空列表时设置为空数组
        }
        spu.setUpdateTime(date);
        return spu;
    }
}