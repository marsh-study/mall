package com.mall.backend.model.vo;

import com.mall.backend.model.entity.UmsMember;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    private Long id;
    private Integer gender;
    private String nickName;
    private String mobile;
    private Date birthday;
    private String avatarUrl;
    private String openid;
    private String sessionKey;
    private String city;
    private String country;
    private String language;
    private String province;
    private Integer status;
    private Double balance;
    private Integer deleted;
    private Integer point;
    private List<Address> addressList;

    public static Member fromEntity(UmsMember entity) {
        Member member = new Member();
        member.setId(entity.getId());
        member.setGender(entity.getGender());
        member.setNickName(entity.getNickName());
        member.setMobile(entity.getMobile());
        member.setBirthday(entity.getBirthday());
        member.setAvatarUrl(entity.getAvatarUrl());
        member.setOpenid(entity.getOpenid());
        member.setSessionKey(entity.getSessionKey());
        member.setCity(entity.getCity());
        member.setCountry(entity.getCountry());
        member.setLanguage(entity.getLanguage());
        member.setProvince(entity.getProvince());
        member.setStatus(entity.getStatus());
        member.setBalance(entity.getBalance());
        member.setDeleted(entity.getDeleted());
        member.setPoint(entity.getPoint());
        member.setAddressList(
                entity.getAddressList().stream().map(Address::fromEntity).toList()
        );
        return member;
    }
}
