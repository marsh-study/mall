package com.mall.backend.util;

import lombok.Data;

import java.util.List;

/**
 * 分页结果类
 *
 * @param <T> 分页数据类型
 */
@Data
public class PageResult<T> {
    /**
     * 分页数据列表
     */
    private List<T> list;
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 构造函数
     *
     * @param list 分页数据列表
     * @param total   总记录数
     */
    public PageResult(List<T> list, Long total) {
        this.list = list;
        this.total = total;
    }

}
