package com.mall.backend.model.vo;

import com.mall.backend.model.entity.UmsAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private Long id;
    private Long memberId;
    private String consigneeName;
    private String consigneeMobile;
    private String province;
    private String city;
    private String area;
    private String detailAddress;
    private String zipCode;
    private Integer defaulted;

    public static Address fromEntity(UmsAddress address) {

        return new Address(address.getId(), address.getMemberId(), address.getConsigneeName(), address.getConsigneeMobile(), address.getProvince(), address.getCity(), address.getArea(), address.getDetailAddress(), address.getZipCode(), address.getDefaulted());
    }
}
