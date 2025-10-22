package com.mall.backend.model.form;

import com.mall.backend.model.entity.PmsSpuAttribute;
import lombok.Data;

import java.util.Date;

@Data
public class AttributeForm {
    private Long selfId;
    private Long id; // 属性ID
    private String name; // 属性名称
    private String value; // 属性值
    private Long categoryId; // 分类ID
    private Long spuId; // 商品ID

    public static AttributeForm fromEntity(PmsSpuAttribute entity) {
        AttributeForm form = new AttributeForm();
        form.setId(entity.getId());
        form.setName(entity.getName());
        form.setValue(entity.getValue());
        form.setSpuId(entity.getSpuId());
        return form;
    }

    public PmsSpuAttribute toPmsSpuAttribute() {
        PmsSpuAttribute attribute = new PmsSpuAttribute();
        attribute.setName(name);
        attribute.setValue(value);
        attribute.setAttributeId(id);
        attribute.setSpuId(spuId);
        attribute.setType(2);
        attribute.setCreateTime(new Date());
        attribute.setUpdateTime(new Date());
        return attribute;
    }

    public PmsSpuAttribute toPmsSpuAttribute(Date date) {
        PmsSpuAttribute attribute = new PmsSpuAttribute();
        attribute.setId(selfId);
        attribute.setName(name);
        attribute.setValue(value);
        attribute.setType(2);
        attribute.setAttributeId(id);
        attribute.setSpuId(spuId);
        attribute.setUpdateTime(date);
        return attribute;
    }
}
