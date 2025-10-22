package com.mall.backend.model.form;

import com.mall.backend.model.entity.SysDict;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictForm {
/**
     * 字典ID
     */
    private Long id;
    /**
     * 字典名称
     */
    private String name;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 状态(1:启用;0:禁用)
     */
    private Integer status;
    /**
     * 类型编码
     */
    private String typeCode;
    /**
     * 值
     */
    private String value;
    /**
     * 备注
     */
    private String remark;

    public SysDict toSysDict(){
        SysDict sysDict = new SysDict();
        sysDict.setId(id);
        sysDict.setName(name);
        sysDict.setSort(sort);
        sysDict.setStatus(status);
        sysDict.setTypeCode(typeCode);
        sysDict.setValue(value);
        sysDict.setRemark(remark);
        sysDict.setUpdateTime(new Date());
        sysDict.setCreateTime(new Date());
        return sysDict;
    }

    public SysDict toSysDict(Date updateTime){
        SysDict sysDict = new SysDict();
        sysDict.setId(id);
        sysDict.setName(name);
        sysDict.setSort(sort);
        sysDict.setStatus(status);
        sysDict.setTypeCode(typeCode);
        sysDict.setValue(value);
        sysDict.setRemark(remark);
        sysDict.setUpdateTime(new Date());
        return sysDict;
    }
}
