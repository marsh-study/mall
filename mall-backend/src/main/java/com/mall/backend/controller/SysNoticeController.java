package com.mall.backend.controller;

import com.mall.backend.model.entity.SysNotice;
import com.mall.backend.model.form.NoticeForm;
import com.mall.backend.model.query.NoticeQuery;
import com.mall.backend.model.vo.NoticePageVO;
import com.mall.backend.service.system.SysNoticeService;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 通知公告表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2025-10-14
 */
@RestController
@RequestMapping("/notices")
public class SysNoticeController {

    @Autowired
    private SysNoticeService sysNoticeService;

    @GetMapping("/page")
    public Result<PageResult<NoticePageVO>> page(NoticeQuery query){
        if (query == null){
            return Result.fail("参数错误");
        }
        return sysNoticeService.getPage(query);
    }

    /**
     * 新增通知公告
     * @param noticeForm
     * @return
     */
    @PostMapping
    public Result addNotice(@RequestBody NoticeForm noticeForm){

        SysNotice entity = noticeForm.toEntity();
        return sysNoticeService.addNotice(entity);
    }
}
