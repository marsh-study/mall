package com.mall.backend.service.system;

import com.mall.backend.model.entity.SysNotice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.backend.model.form.NoticeForm;
import com.mall.backend.model.query.NoticeQuery;
import com.mall.backend.model.vo.NoticePageVO;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;

/**
 * <p>
 * 通知公告表 服务类
 * </p>
 *
 * @author lsy
 * @since 2025-10-14
 */
public interface SysNoticeService extends IService<SysNotice> {

    Result<PageResult<NoticePageVO>> getPage(NoticeQuery query);

    Result addNotice(SysNotice entity);
}
