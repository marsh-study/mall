package com.mall.backend.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Brand {

    private Long id;
    private String name;
    private String logoUrl;
    private Integer sort;
}
