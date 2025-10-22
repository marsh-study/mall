package com.mall.backend.service.system;

import com.mall.backend.model.entity.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.backend.model.query.LogQuery;
import com.mall.backend.model.vo.LogVO;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lsy
 * @since 2025-10-13
 */
public interface SysLogService extends IService<SysLog> {


    Result<PageResult<LogVO>> getLogPage(LogQuery queryParams);
}
