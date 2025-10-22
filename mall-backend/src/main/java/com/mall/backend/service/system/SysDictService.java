package com.mall.backend.service.system;

import com.mall.backend.common.model.OptionType;
import com.mall.backend.model.entity.SysDict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.backend.model.form.DictForm;
import com.mall.backend.model.query.DictTypeQuery;
import com.mall.backend.model.vo.DictPageVO;
import com.mall.backend.model.vo.DictTypePageVO;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;

import java.util.List;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author lsy
 * @since 2025-09-30
 */
public interface SysDictService extends IService<SysDict> {

    List<OptionType> getDictOptions(String typeCode);

    Result<PageResult<DictPageVO>> getDictPage(DictTypeQuery dictQuery);

    Result<DictForm> getDictForm(Long id);

    Result addDict(DictForm dictForm);


    Result updateDict(SysDict sysDict);

    Result deleteDict(List<Long> idList);
}
