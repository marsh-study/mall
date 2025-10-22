package com.mall.backend.service.ums.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.backend.mapper.ums.UmsMemberMapper;
import com.mall.backend.model.entity.UmsAddress;
import com.mall.backend.model.entity.UmsMember;
import com.mall.backend.model.query.MemberQuery;
import com.mall.backend.model.vo.Member;
import com.mall.backend.service.ums.UmsAddressService;
import com.mall.backend.service.ums.UmsMemberService;
import com.mall.backend.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lsy
 * @since 2025-10-20
 */
@Service
public class UmsMemberServiceImpl extends ServiceImpl<UmsMemberMapper, UmsMember> implements UmsMemberService {

    @Autowired
    private UmsAddressService umsAddressService;
    @Override
    public PageResult<Member> getMemberPage(MemberQuery query) {
        Page page = new Page(query.getPageNum(), query.getPageSize());
        Page result = this.page(page, new QueryWrapper<UmsMember>().like(query.getNickName() != null, "nick_name", query.getNickName()));

        //获取会员id列表
        List<UmsMember> memberList = result.getRecords();
        List<Long> memberIdList = memberList.stream().map(UmsMember::getId).collect(Collectors.toList());

        // 根据id查询会员地址
        List<UmsAddress> umsAddressList = new ArrayList<>() ;
        if (!memberIdList.isEmpty()) {
            umsAddressList = umsAddressService.list(new QueryWrapper<UmsAddress>().in("member_id", memberIdList));
        }

        // 构建会员ID到地址列表的映射
        Map<Long, List<UmsAddress>> addressMap = umsAddressList.stream()
            .collect(Collectors.groupingBy(UmsAddress::getMemberId));

        // 正确设置每个会员的地址列表
        memberList.forEach(member ->
            member.setAddressList(addressMap.getOrDefault(member.getId(), Collections.emptyList()))
        );

        List<Member> members = memberList.stream().map(Member::fromEntity).collect(Collectors.toList());
        return new PageResult<>(members, result.getTotal());

    }
}
