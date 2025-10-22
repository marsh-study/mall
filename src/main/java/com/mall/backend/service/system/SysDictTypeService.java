package com.mall.backend.service.system;

import com.mall.backend.model.entity.SysDictType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.backend.model.form.DictTypeForm;
import com.mall.backend.model.query.DictTypeQuery;
import com.mall.backend.model.vo.DictTypePageVO;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;

import java.util.List;

/**
 * <p>
 * 字典类型表 服务类
 * </p>
 *
 * @author lsy
 * @since 2025-10-11
 */
public interface SysDictTypeService extends IService<SysDictType> {

    Result<PageResult<DictTypePageVO>> getDictTypePage(DictTypeQuery dictTypeQuery);

    Result addDictType(DictTypeForm dictTypeForm);

    Result<DictTypeForm> getDictTypeForm(Long id);

    Result updateDictType(SysDictType sysDictType);


    Result deleteDictType(List<Long> idList);
}
