package com.mall.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.mall.backend.common.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author lsy
 * @since 2025-10-20
 */
@Getter
@Setter
@TableName("ums_member")
@AllArgsConstructor
@NoArgsConstructor
public class UmsMember extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private  Integer gender;

    private String nickName;

    private String mobile;

    private Date birthday;

    private String avatarUrl;

    private String openid;

    private String sessionKey;

    private Integer status;

    /**
     * 会员积分
     */
    private Integer point;

    private Integer deleted;

    private Double balance;

    private String city;

    private String country;

    private String language;

    private String province;

    @TableField(exist = false)
    private List<UmsAddress> addressList;
}
