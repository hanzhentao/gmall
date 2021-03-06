package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.SkuInfoEntity;
import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuInfoVO extends SkuInfoEntity {

    private BigDecimal growBounds;
    private BigDecimal buyBounds;
    private List<Integer> work;

    private Integer fullCount;
    private BigDecimal discount;
    private BigDecimal price;
    private Integer ladderaddOther;

    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer fulladdOther;

    private List<SkuSaleAttrValueEntity> saleAttrs;

    private List<String> images;
}
