package com.mall.backend.controller;

import com.mall.backend.model.query.LogQuery;
import com.mall.backend.model.vo.LogVO;
import com.mall.backend.service.system.SysLogService;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lsy
 * @since 2025-10-13
 */
@RestController
@RequestMapping("/logs")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    /**
     * 查询日志
     * @param queryParams
     * @return List<LogVO>
     */
    @GetMapping
    public Result<PageResult<LogVO>> getLogPage(LogQuery queryParams) {
        if (queryParams == null){
            return Result.fail("参数错误");
        }
        return sysLogService.getLogPage(queryParams);
    }
}
