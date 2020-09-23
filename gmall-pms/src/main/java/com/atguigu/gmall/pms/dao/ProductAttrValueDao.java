package com.atguigu.gmall.pms.dao;

import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * spu属性值
 * 
 * @author hanzhentao
 * @email panbubblepanbubble@163.com
 * @date 2020-09-13 12:00:54
 */
@Mapper
public interface ProductAttrValueDao extends BaseMapper<ProductAttrValueEntity> {
    //@Select("select a.attr_id,a.attr_name,a.attr_value from pms_product_attr_value a INNER JOIN pms_attr b on a.attr_id=b.attr_id where b.search_type=1 and a.spu_id=#{spuId}")
    @Select("select a.a* from pms_product_attr_value a INNER JOIN pms_attr b on a.attr_id=b.attr_id where b.search_type=1 and a.spu_id=#{spuId}")
    List<ProductAttrValueEntity> querySearchAttrValueBySpuId(Long spuId);
}
