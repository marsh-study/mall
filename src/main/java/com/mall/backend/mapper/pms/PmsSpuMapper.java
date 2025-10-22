package com.mall.backend.mapper.pms;

import com.mall.backend.model.entity.PmsSpu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.backend.model.query.GoodsQuery;
import com.mall.backend.model.vo.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
@Mapper
public interface PmsSpuMapper extends BaseMapper<PmsSpu> {

    /**
     * 链表查询商品信息（带分页）
     * @param query 查询条件
     * @return 商品列表
     */
    List<Goods> selectGoodsPage(@Param("query") GoodsQuery query);


    /**
     * 统计链表查询的商品总数
     * @param query 查询条件
     * @return 总数
     */
    Long selectGoodsCount(@Param("query") GoodsQuery query);

}
