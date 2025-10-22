package com.mall.backend.common.model;

import com.mall.backend.model.entity.PmsCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OptionType<T>  implements java.io.Serializable{
    /** 值 */
    private T value;
    /** 文本 */
    private String label;
    /** 子列表  */
    private List<OptionType<T>> children;

    public OptionType(T value, String label) {
        this.value = value;
        this.label = label;
    }


}
