package com.mall.backend.service.pms.impl;

import com.mall.backend.common.model.OptionType;
import com.mall.backend.model.entity.PmsCategory;
import com.mall.backend.mapper.pms.PmsCategoryMapper;
import com.mall.backend.model.vo.CategoryVO;
import com.mall.backend.service.pms.PmsCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.backend.util.Result;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品分类表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
@Service
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryMapper, PmsCategory> implements PmsCategoryService {

    @Override
    @Cacheable(value = "categoryOptions")
    public List<OptionType> getCategoryOptions() {
        // 查询所有分类
        List<PmsCategory> categoryList = this.list();
        List<OptionType> optionTypeList = buildCategory(categoryList,0L);
        return optionTypeList;
    }



    private List<OptionType> buildCategory(List<PmsCategory> categoryList, long l) {
    // 空值检查
    if (categoryList == null || categoryList.isEmpty()) {
        return new ArrayList<>();
    }

    List<OptionType> optionTypeList = new ArrayList<>();
    for (PmsCategory category : categoryList) {
        if (category == null) {
            continue;
        }
        if (category.getParentId() == l) {
            OptionType optionType = new OptionType(category.getId(), category.getName());
            optionType.setChildren(buildCategory(categoryList, category.getId()));
            // 修复：将创建的对象添加到列表中
            optionTypeList.add(optionType);
        }
    }
    return optionTypeList;
}

    @Override
    public List<CategoryVO> getCategoryTree(List<PmsCategory> list) {
        List<CategoryVO> categoryVOList=buildCategoryTree(list,0L);
        return categoryVOList;
    }

    private List<CategoryVO> buildCategoryTree(List<PmsCategory> list, long l) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        List<CategoryVO> categoryVOList = new ArrayList<>();
        for (PmsCategory category : list) {
            if (category == null) {
                continue;
            }
            if (category.getParentId() == l) {
                CategoryVO categoryVO = new CategoryVO();
                categoryVO.setId(category.getId());
                categoryVO.setName(category.getName());
                categoryVO.setParentId(category.getParentId());
                categoryVO.setLevel(category.getLevel());
                categoryVO.setChildren(buildCategoryTree(list, category.getId()));
                categoryVOList.add(categoryVO);
            }
        }
        return categoryVOList;
    }

}
