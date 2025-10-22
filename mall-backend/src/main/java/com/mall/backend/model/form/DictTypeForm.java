package com.mall.backend.model.form;

import com.mall.backend.model.entity.SysDictType;
import lombok.Data;

import java.util.Date;

@Data
public class DictTypeForm {

    /**
     * 字典类型ID
     */
    private Long id;
    /**
     * 类型名称
     */
    private String name;
    /**
     * 类型编码
     */
    private String code;
    /**
     * 类型状态：1:启用;0:禁用
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

    public SysDictType toSysDictType() {
        SysDictType sysDictType = new SysDictType();
        sysDictType.setId(this.id);
        sysDictType.setName(this.name);
        sysDictType.setCode(this.code);
        sysDictType.setStatus(this.status);
        sysDictType.setRemark(this.remark);
        sysDictType.setCreateTime(new Date());
        sysDictType.setUpdateTime(new Date());
        return sysDictType;
    }

    public SysDictType toSysDictType(Date date) {
        SysDictType sysDictType = new SysDictType();
        sysDictType.setId(this.id);
        sysDictType.setName(this.name);
        sysDictType.setCode(this.code);
        sysDictType.setStatus(this.status);
        sysDictType.setRemark(this.remark);
        sysDictType.setUpdateTime(date);
        return sysDictType;
    }
}
