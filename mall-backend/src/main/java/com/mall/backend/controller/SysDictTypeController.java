package com.mall.backend.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mall.backend.annotation.SystemLog;
import com.mall.backend.common.model.OptionType;
import com.mall.backend.exception.BusinessException;
import com.mall.backend.model.entity.SysDict;
import com.mall.backend.model.entity.SysDictType;
import com.mall.backend.model.form.DictTypeForm;
import com.mall.backend.model.query.DictTypeQuery;
import com.mall.backend.model.vo.DictTypePageVO;
import com.mall.backend.service.system.SysDictService;
import com.mall.backend.service.system.SysDictTypeService;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典类型表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2025-10-11
 */
@RestController
@RequestMapping("/dict/types")
public class SysDictTypeController {
    @Autowired
    private SysDictTypeService dictTypeService;

    @Autowired
    private SysDictService dictService;

    @GetMapping("/{typeCode}/items")
    public Result<List<OptionType>> getDictOptions(@PathVariable("typeCode") String typeCode) {
        List<OptionType> optionTypeList = dictService.getDictOptions(typeCode);
        return Result.success(optionTypeList);
    }


    /**
     * 字典类型分页列表
     */
    @GetMapping("/page")
    public Result<PageResult<DictTypePageVO>> getDictTypePage(DictTypeQuery dictTypeQuery) {
        if (dictTypeQuery == null){
            return Result.fail("参数错误");
        }
        return dictTypeService.getDictTypePage(dictTypeQuery);
    }

    /**
     * 新增字典类型
     * @param dictTypeForm
     * @return
     */

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog(module = "字典类型", operation = "添加字典类型")
    public Result addDictType(@RequestBody DictTypeForm dictTypeForm) {
        if (dictTypeForm == null){
            return Result.fail("参数错误");
        }
        return dictTypeService.addDictType(dictTypeForm);
    }

    /**
     * 字典类型表单数据
     * @param id
     * @return DictTypeForm
     */
    @GetMapping("/{id}/form")
    public Result<DictTypeForm> getDictTypeForm(@PathVariable("id") Long id) {
        if (id == null){
            return Result.fail("参数错误");
        }

        return dictTypeService.getDictTypeForm(id);
    }

    /**
     * 修改字典类型
     * @param dictTypeForm
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog(module = "字典类型", operation = "修改字典类型")
    @Transactional(rollbackFor = Exception.class)
    public Result updateDictType(@RequestBody DictTypeForm dictTypeForm, @PathVariable("id") Long id) {
        if (dictTypeForm == null || id == null){
            return Result.fail("参数错误");
        }
        //先修改字典数据
        String oldCode = dictTypeService.getById(id).getCode();
        dictService.update(new UpdateWrapper<SysDict>()
                .eq("type_code", oldCode)
                .set("type_code", dictTypeForm.getCode())
                .set("update_time", new Date())
        );

        //再修改字典类型
        SysDictType sysDictType = dictTypeForm.toSysDictType(new Date());
        return dictTypeService.updateDictType(sysDictType);
    }

    /**
     * 删除字典类型
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog(module = "字典类型", operation = "删除字典类型")
    public Result deleteDictType(@PathVariable("ids") String ids) {
        if (ids == null){
            return Result.fail("参数错误");
        }
        List<Long> idList= Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        return dictTypeService.deleteDictType(idList);
    }


}
