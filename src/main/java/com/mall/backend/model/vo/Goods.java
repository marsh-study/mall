package com.mall.backend.model.vo;

import com.mall.backend.model.entity.PmsSku;
import com.mall.backend.model.entity.PmsSpu;
import lombok.Data;

import java.util.List;

@Data
public class Goods {
    private Long id;
    private String name;
    private Long categoryId;
    private Long brandId;
    private String originPrice;
    private String price;
    private Integer sales;
    private String picUrl;
    private String album;
    private String unit;
    private String description;
    private String detail;
    private Integer status;
    private String categoryName;
    private String brandName;
    private List<PmsSku> skuList;

    public static Goods fromEntity(PmsSpu spu) {
        Goods goods = new Goods();
        goods.setId(spu.getId());
        goods.setName(spu.getName());
        goods.setCategoryId(spu.getCategoryId());
        goods.setBrandId(spu.getBrandId());
        goods.setOriginPrice(String.valueOf(spu.getOriginPrice()));
        goods.setPrice(String.valueOf(spu.getPrice()));
        goods.setSales(spu.getSales());
        goods.setPicUrl(spu.getPicUrl());
        goods.setAlbum(spu.getAlbum());
        goods.setUnit(spu.getUnit());
        goods.setDescription(spu.getDescription());
        goods.setDetail(spu.getDetail());
        goods.setStatus(spu.getStatus());

        goods.setSkuList(spu.getSkuList());
        return goods;
    }
}
