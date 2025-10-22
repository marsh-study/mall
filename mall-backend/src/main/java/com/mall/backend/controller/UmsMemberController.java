package com.mall.backend.controller;

import com.mall.backend.model.query.MemberQuery;
import com.mall.backend.model.vo.Member;
import com.mall.backend.service.system.UserService;
import com.mall.backend.service.ums.UmsMemberService;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lsy
 * @since 2025-10-20
 */
@RestController
@RequestMapping("/members")
public class UmsMemberController {

    @Autowired
    private UmsMemberService memberService;

    @GetMapping
    public Result<PageResult<Member>> getMemberPage(MemberQuery query){
        PageResult<Member> pageResult =memberService.getMemberPage(query);
        return Result.success(pageResult);
    }

}
