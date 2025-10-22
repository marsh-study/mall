package com.mall.backend.model.vo;

public class AttributeVO {

    private Long id;
    private String name;
    private String value;
    private Long categoryId;

    // 构造函数
    public AttributeVO() {}

    public AttributeVO(Long id, String name, String value, Long categoryId) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.categoryId = categoryId;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
