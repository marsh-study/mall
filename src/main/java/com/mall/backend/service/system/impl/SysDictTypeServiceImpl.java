package com.mall.backend.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.backend.common.model.OptionType;
import com.mall.backend.exception.BusinessException;
import com.mall.backend.model.entity.SysDict;
import com.mall.backend.model.entity.SysDictType;
import com.mall.backend.mapper.system.SysDictTypeMapper;
import com.mall.backend.model.form.DictTypeForm;
import com.mall.backend.model.query.DictTypeQuery;
import com.mall.backend.model.vo.DictTypePageVO;
import com.mall.backend.service.system.SysDictService;
import com.mall.backend.service.system.SysDictTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典类型表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2025-10-11
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {

    @Autowired
    private SysDictService dictService;

    @Override
    public Result<PageResult<DictTypePageVO>> getDictTypePage(DictTypeQuery dictTypeQuery) {
        Page<SysDictType> page = new Page<>(dictTypeQuery.getPageNum(), dictTypeQuery.getPageSize());
        QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
        if (dictTypeQuery.getKeywords() != null){
            queryWrapper.like("name", dictTypeQuery.getKeywords())
                    .or().like("code", dictTypeQuery.getKeywords());
        }


        Page<SysDictType> sysDictTypePage = this.page(page, queryWrapper);
        List<SysDictType> list = sysDictTypePage.getRecords();
        List<DictTypePageVO> dictTypePageVOList = list.stream().map(item -> {
            DictTypePageVO dictTypePageVO = new DictTypePageVO();
            BeanUtils.copyProperties(item, dictTypePageVO);
            return dictTypePageVO;
        }).collect(Collectors.toList());

        PageResult<DictTypePageVO> pageResult = new PageResult<>(dictTypePageVOList, sysDictTypePage.getTotal());
        return Result.success(pageResult);
    }

    @Override
    public Result addDictType(DictTypeForm dictTypeForm) {
        SysDictType sysDictType = dictTypeForm.toSysDictType();
        boolean save = this.save(sysDictType);
        if (! save){
            throw new BusinessException("新增字典类型失败");
        }
        return Result.success();
    }

    @Override
    public Result<DictTypeForm> getDictTypeForm(Long id) {
        SysDictType dictType = this.getById(id);
        if (dictType == null){
            throw new BusinessException("字典类型不存在");
        }
        DictTypeForm dictTypeForm = new DictTypeForm();
        BeanUtils.copyProperties(dictType, dictTypeForm);
        return Result.success(dictTypeForm);
    }

    @Override
    public Result updateDictType(SysDictType sysDictType) {

        boolean flag = this.updateById(sysDictType);
        if (! flag){
            throw new BusinessException("修改字典类型失败");
        }
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteDictType(List<Long> idList) {
        // 先删除字典项（根据字典类型 code）
        List<String> codeList = this.list(new QueryWrapper<SysDictType>()
                .in("id", idList)
                .select("code"))
                .stream()
                .map(SysDictType::getCode)
                .collect(Collectors.toList());

        // TODO: 根据 codeList 删除关联的 SysDict 数据
        dictService.remove(new QueryWrapper<SysDict>().in("type_code", codeList));

        // 再删除字典类型
        boolean remove = this.removeByIds(idList);
        if (!remove) {
            throw new BusinessException("删除字典类型失败");
        }
        return Result.success();
    }



}
