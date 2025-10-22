package com.mall.backend.service.pms.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mall.backend.exception.BusinessException;
import com.mall.backend.mapper.pms.PmsSpuMapper;
import com.mall.backend.model.entity.MerchantSpu;
import com.mall.backend.model.entity.PmsSku;
import com.mall.backend.model.entity.PmsSpu;
import com.mall.backend.model.entity.PmsSpuAttribute;
import com.mall.backend.model.form.SpuForm;
import com.mall.backend.model.query.GoodsQuery;
import com.mall.backend.model.vo.Goods;
import com.mall.backend.service.pms.MerchantSpuService;
import com.mall.backend.service.pms.PmsSkuService;
import com.mall.backend.service.pms.PmsSpuAttributeService;
import com.mall.backend.service.pms.PmsSpuService;
import com.mall.backend.service.system.UserService;
import com.mall.backend.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
@Service
public class PmsSpuServiceImpl extends ServiceImpl<PmsSpuMapper, PmsSpu> implements PmsSpuService {


    @Autowired
    private PmsSpuAttributeService attributeService;

    @Autowired
    private PmsSkuService skuService;

    @Autowired
    private UserService userService;

    @Autowired
    private MerchantSpuService merchantSpuService;

    @Override
    public PageResult<Goods> getPage(GoodsQuery query) {
        Long currentUserId = userService.getCurrentUserId();

        List<MerchantSpu> merchantSpuList = merchantSpuService.list(new QueryWrapper<MerchantSpu>().eq("merchant_id", currentUserId));
        List<Long> spuId = merchantSpuList.stream().map(MerchantSpu::getSpuId).toList();

        // 查询数据列表
        Page<PmsSpu> pmsSpuPage = this.page(new Page<>(query.getPageNum(), query.getPageSize()),
                new QueryWrapper<PmsSpu>().like(query.getName() != null, "name", query.getName())
                        .like(query.getCategoryId() != null, "category_id", query.getCategoryId())
                        .in("id", spuId)
        );

        List<Goods> goodsList = pmsSpuPage.getRecords().stream().map(Goods::fromEntity).collect(Collectors.toList());
        return new PageResult<>(goodsList, pmsSpuPage.getTotal());
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addSpuWithRelatedData(SpuForm spuForm) {
        PmsSpu pmsSpu = spuForm.toPmsSpu();
        boolean save = this.save(pmsSpu);
        if(!save){
            throw new BusinessException("添加商品失败");
        }

        Long spuId =pmsSpu.getId();

        Long currentUserId = userService.getCurrentUserId();
        merchantSpuService.save(new MerchantSpu(Math.toIntExact(currentUserId),spuId));

        List<PmsSku> pmsSkuList = spuForm.getSkuList().stream().map(item -> {
            item.setSpuId(spuId);
            return item.toEntity();
        }).collect(Collectors.toList());

        List<PmsSpuAttribute> pmsSpuAttributes = spuForm.getAttrList().stream().map(item -> {
            item.setSpuId(spuId);
            return item.toPmsSpuAttribute();
        }).collect(Collectors.toList());

        List<PmsSpuAttribute> pmsSpuAttributes1 = spuForm.getSpecList().stream().map(item -> {
            item.setSpuId(spuId);
            return item.toEntity();
        }).toList();

        pmsSpuAttributes.addAll(pmsSpuAttributes1);

        skuService.addSku(pmsSkuList);
        attributeService.addAttribute(pmsSpuAttributes);
        return true;
    }

    @Override
    public SpuForm getDetail(Long id) throws JsonProcessingException {
        PmsSpu pmsSpu = this.getById(id);
        List<PmsSku> skuList = skuService.list(new QueryWrapper<PmsSku>().eq("spu_id", id));
        List<PmsSpuAttribute> pmsSpuAttributes = attributeService.list(new QueryWrapper<PmsSpuAttribute>().eq("spu_id", id));


        // 按类型分离
        Map<Integer, List<PmsSpuAttribute>> groupedAttributes = pmsSpuAttributes.stream()
                .collect(Collectors.groupingBy(attr -> {
                    // 处理可能的空值情况
                    return attr.getType() != null ? attr.getType() : 0;
                }));
        // 分别处理不同类型的属性
        List<PmsSpuAttribute> specList = groupedAttributes.getOrDefault(1, new ArrayList<>());
        List<PmsSpuAttribute> attributeList = groupedAttributes.getOrDefault(2, new ArrayList<>());

        pmsSpu.setSkuList(skuList);
        pmsSpu.setAttributeList(attributeList);
        pmsSpu.setSpecList(specList);
        return SpuForm.fromEntity(pmsSpu);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSpuWithRelatedData(SpuForm spuForm) {
        PmsSpu pmsSpu = spuForm.toPmsSpu(new Date());
        boolean update = this.updateById(pmsSpu);
        if(!update){
            throw new BusinessException("更新商品失败");
        }
        Long spuId =pmsSpu.getId();
        List<PmsSku> pmsSkuList = spuForm.getSkuList().stream().map(item -> {
            item.setSpuId(spuId);
            return item.toEntity();
        }).collect(Collectors.toList());
        skuService.remove(new QueryWrapper<PmsSku>().eq("spu_id", spuId));
        skuService.addSku(pmsSkuList);
        
        // 先删除原有的规格和属性记录
        attributeService.remove(new QueryWrapper<PmsSpuAttribute>().eq("spu_id", spuId));
        
        // 重新添加所有属性和规格记录
        List<PmsSpuAttribute> pmsSpuAttributes = spuForm.getAttrList().stream().map(item -> {
            item.setSpuId(spuId);
            return item.toPmsSpuAttribute(new Date());
        }).collect(Collectors.toList());
        List<PmsSpuAttribute> pmsSpuAttributes1 = spuForm.getSpecList().stream().map(item -> {
            item.setSpuId(spuId);
            return item.toEntity(new Date());
        }).toList();
        pmsSpuAttributes.addAll(pmsSpuAttributes1);
        attributeService.addAttribute(pmsSpuAttributes);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeSpuByIds(List<Long> idList) {
        // 先删除merchant_spu关联表中的记录
        merchantSpuService.remove(new QueryWrapper<MerchantSpu>().in("spu_id", idList));

        // 删除商品相关的sku和属性
        skuService.remove(new QueryWrapper<PmsSku>().in("spu_id", idList));
        attributeService.remove(new QueryWrapper<PmsSpuAttribute>().in("spu_id", idList));

        boolean remove = this.removeByIds(idList);
        if (!remove) {
            throw new BusinessException("删除商品失败");
        }
        //继续删除关联数据
        skuService.remove(new QueryWrapper<PmsSku>().in("spu_id", idList));
        attributeService.remove(new QueryWrapper<PmsSpuAttribute>().in("spu_id", idList));
        return true;
    }
}
