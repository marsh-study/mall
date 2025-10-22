package com.mall.backend.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.backend.common.model.OptionType;
import com.mall.backend.exception.BusinessException;
import com.mall.backend.model.entity.SysDict;
import com.mall.backend.mapper.system.SysDictMapper;
import com.mall.backend.model.form.DictForm;
import com.mall.backend.model.query.DictTypeQuery;
import com.mall.backend.model.vo.DictPageVO;
import com.mall.backend.model.vo.DictTypePageVO;
import com.mall.backend.service.system.SysDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2025-09-30
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    @Override
    @Cacheable(cacheNames = "dictOptions", key = "#typeCode",unless = "#result == null" )
    public List<OptionType> getDictOptions(String typeCode) {
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<SysDict>()
                .eq("type_code", typeCode)
                .eq("status", 1)
                .orderByAsc("sort");
        List<SysDict> dictList = this.list(queryWrapper);
        if (dictList.isEmpty()) {
            return new ArrayList<>();
        }
        List<OptionType> optionTypeList = dictList.stream().map(item -> new OptionType(item.getValue(), item.getName(), null)).toList();
        return optionTypeList;
    }

    @Override
    public Result<PageResult<DictPageVO>> getDictPage(DictTypeQuery dictQuery) {
        Page<SysDict> page = new Page<>(dictQuery.getPageNum(), dictQuery.getPageSize());
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<SysDict>();
        queryWrapper.eq("type_code", dictQuery.getTypeCode());
        queryWrapper.like(dictQuery.getKeywords()!=null,"name", dictQuery.getKeywords())
                .orderByAsc("sort");
        Page<SysDict> sysDictPage = this.page(page, queryWrapper);
        List<SysDict> records = sysDictPage.getRecords();
        List<DictPageVO> dictPageVOList = records.stream().map(item -> {
            DictPageVO dictPageVO = new DictPageVO();
            BeanUtils.copyProperties(item, dictPageVO);
            return dictPageVO;
        }).toList();
        return Result.success(new PageResult<>(dictPageVOList, sysDictPage.getTotal()));
    }

    @Override
    public Result<DictForm> getDictForm(Long id) {
        SysDict sysDict = this.getById(id);
        if (sysDict == null){
            throw new BusinessException("字典不存在");
        }
        DictForm dictForm = new DictForm();
        BeanUtils.copyProperties(sysDict, dictForm);
        return Result.success(dictForm);
    }

    @Override
    public Result addDict(DictForm dictForm) {
        SysDict sysDict = dictForm.toSysDict();
        boolean save = this.save(sysDict);
        if(!save){
            throw new BusinessException("添加字典失败");
        }
        return Result.success();
    }

    @Override
    @CacheEvict(cacheNames = "dictOptions", key = "#sysDict.typeCode")
    public Result updateDict(SysDict sysDict) {
        boolean flag = this.updateById(sysDict);
        if(!flag){
            throw new BusinessException("更新字典失败");
        }
        return Result.success();

    }

    @Override
    public Result deleteDict(List<Long> idList) {
        boolean remove = this.removeByIds(idList);
        if (!remove) {
            throw new BusinessException("删除字典失败");
        }
        return Result.success();
    }


}
