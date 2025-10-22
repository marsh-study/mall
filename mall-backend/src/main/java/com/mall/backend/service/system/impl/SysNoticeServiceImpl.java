package com.mall.backend.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.backend.model.entity.SysNotice;
import com.mall.backend.mapper.system.SysNoticeMapper;
import com.mall.backend.model.form.NoticeForm;
import com.mall.backend.model.query.NoticeQuery;
import com.mall.backend.model.vo.NoticePageVO;
import com.mall.backend.service.system.SysNoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 通知公告表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2025-10-14
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService {

    @Override
    public Result<PageResult<NoticePageVO>> getPage(NoticeQuery query) {
        Page<SysNotice> page = new Page<>(query.getPageNum(), query.getPageSize());
        QueryWrapper<SysNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(query.getTitle() != null, "title", query.getTitle());
        queryWrapper.eq(query.getType() != null, "type", query.getType());
        queryWrapper.eq(query.getPublisher() != null, "publisher", query.getPublisher());
        queryWrapper.like(query.getLevel() != null, "level", query.getLevel());
        queryWrapper.eq(query.getStatus() != null, "status", query.getStatus());
        Page<SysNotice> pageResult = this.page(page, queryWrapper);
        List<NoticePageVO> collect = pageResult.getRecords().stream().map(NoticePageVO::fromEntity).collect(Collectors.toList());
        return Result.success(new PageResult<>(collect, pageResult.getTotal()));
    }

    @Override
    public Result addNotice(SysNotice entity) {
        boolean save = this.save(entity);
        if (!save) {
            return Result.fail("添加失败");
        }
        return Result.success();
    }
}
