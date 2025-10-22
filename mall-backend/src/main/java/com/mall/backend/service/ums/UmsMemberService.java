package com.mall.backend.service.ums;

import com.mall.backend.model.entity.UmsMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.backend.model.query.MemberQuery;
import com.mall.backend.model.vo.Member;
import com.mall.backend.util.PageResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lsy
 * @since 2025-10-20
 */
public interface UmsMemberService extends IService<UmsMember> {

    PageResult<Member> getMemberPage(MemberQuery query);
}
