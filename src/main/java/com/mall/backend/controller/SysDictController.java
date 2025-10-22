package com.mall.backend.controller;

import com.mall.backend.annotation.SystemLog;
import com.mall.backend.common.model.OptionType;
import com.mall.backend.exception.BusinessException;
import com.mall.backend.model.entity.SysDict;
import com.mall.backend.model.form.DictForm;
import com.mall.backend.model.query.DictTypeQuery;
import com.mall.backend.model.vo.DictPageVO;
import com.mall.backend.model.vo.DictTypePageVO;
import com.mall.backend.service.system.SysDictService;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典数据表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2025-09-30
 */
@RestController
@RequestMapping("/dict")
public class SysDictController {
    @Autowired
    private SysDictService dictService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 获取字典分页数据
     * @param dictQuery
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult<DictPageVO>> getDictPage(DictTypeQuery dictQuery){
        if (dictQuery == null){
            throw new BusinessException("参数错误");
        }
        return dictService.getDictPage(dictQuery);
    }

    /**
     * 获取字典表单数据
     * @param id
     * @return DictForm
     */
    @GetMapping("/{id}/form")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<DictForm> getDictForm(@PathVariable Long id){
        if (id == null){
            throw new BusinessException("参数错误");
        }
        return dictService.getDictForm(id);
    }

    /**
     * 添加字典
     * @param dictForm
     * @return
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog(module = "字典管理", operation = "添加字典")
    public Result addDict(@RequestBody DictForm dictForm){
        if (dictForm == null){
            throw new BusinessException("参数错误");
        }
        return dictService.addDict(dictForm);
    }

    /**
     * 修改字典
     * @param dictForm
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog(module = "字典管理", operation = "修改字典")
    public Result updateDict(@RequestBody DictForm dictForm, @PathVariable Long id){
        if (dictForm == null|| id == null){
            throw new BusinessException("参数错误");
        }
        dictForm.setId(id);
        SysDict sysDict = dictForm.toSysDict(new Date());
        return dictService.updateDict(sysDict);
    }

    /**
     * 删除字典
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog(module = "字典管理", operation = "删除字典")
    public Result deleteDict(@PathVariable String ids){
        if (ids == null){
            throw new BusinessException("参数错误");
        }
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());

        return dictService.deleteDict(idList);
    }

}