package com.mall.backend.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.backend.model.entity.SysLog;
import com.mall.backend.mapper.system.SysLogMapper;
import com.mall.backend.model.query.LogQuery;
import com.mall.backend.model.vo.LogVO;
import com.mall.backend.service.system.SysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lsy
 * @since 2025-10-13
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Override
    public Result<PageResult<LogVO>> getLogPage(LogQuery queryParams) {
        Page page = new Page<>(queryParams.getPageNum(), queryParams.getPageSize());
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(queryParams.getModule() != null, "module", queryParams.getModule())
                .like(queryParams.getUsername() != null, "username", queryParams.getUsername())
                .orderByDesc("create_time");

        if(queryParams.getStartTime()!=null&&queryParams.getEndTime()!=null){
            queryWrapper.between("create_time", queryParams.getStartTime(), queryParams.getEndTime());
        }
        Page<SysLog> sysLogPage = this.page(page, queryWrapper);
        List<LogVO> logVOList = sysLogPage.getRecords().stream().map(item ->{
            LogVO logVO = new LogVO();
            BeanUtils.copyProperties(item, logVO);
            return logVO;
        }).collect(Collectors.toList());
        return Result.success(new PageResult<>(logVOList, sysLogPage.getTotal()));
        }

}
