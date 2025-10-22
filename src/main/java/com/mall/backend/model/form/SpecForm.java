package com.mall.backend.model.form;

import com.mall.backend.model.entity.PmsSpuAttribute;
import lombok.Data;

import java.util.Date;

@Data
public  class SpecForm {
    private String id; // 规格ID
    private String name; // 规格名称
    private String value; // 规格值
    private String picUrl; // 规格对应的图片URL
    private Long spuId; // 商品ID

    public static SpecForm fromEntity(PmsSpuAttribute entity) {
        SpecForm form = new SpecForm();
        form.setId(entity.getId().toString());
        form.setName(entity.getName());
        form.setValue(entity.getValue());
        form.setPicUrl(entity.getPicUrl());
        form.setSpuId(entity.getSpuId());
        return form;
    }

    public PmsSpuAttribute toEntity() {
        PmsSpuAttribute entity = new PmsSpuAttribute();
        entity.setName(name);
        entity.setValue(value);
        entity.setPicUrl(picUrl);
        entity.setType(1);
        entity.setSpuId(spuId);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        return entity;
    }

    public PmsSpuAttribute toEntity(Date date) {
        PmsSpuAttribute entity = new PmsSpuAttribute();
        // 只有当id是数字时才设置entity的id字段
        if (id != null && id.matches("\\d+")) {
            entity.setId(Long.valueOf(id));
        }
        entity.setName(name);
        entity.setValue(value);
        entity.setPicUrl(picUrl);
        entity.setType(1);
        entity.setSpuId(spuId);
        entity.setUpdateTime(date);
        return entity;
    }
}