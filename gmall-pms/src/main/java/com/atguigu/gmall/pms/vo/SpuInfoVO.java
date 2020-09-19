package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import lombok.Data;

import java.util.List;
@Data
public class SpuInfoVO extends SpuInfoEntity {
    List<String> spuImages;
    List<BaseAttrVO> baseAttrs;
    List<SkuInfoVO> skus;
}
