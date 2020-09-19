package com.atguigu.gmall.pms.service.impl;

import com.atguigu.core.bean.PageMaker;
import com.atguigu.gmall.pms.dao.ProductAttrValueDao;
import com.atguigu.gmall.pms.dao.SpuInfoDescDao;
import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import com.atguigu.gmall.pms.entity.SpuInfoDescEntity;
import com.atguigu.gmall.pms.service.ProductAttrValueService;
import com.atguigu.gmall.pms.vo.BaseAttrVO;
import com.atguigu.gmall.pms.vo.SpuInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.SpuInfoDao;
import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import com.atguigu.gmall.pms.service.SpuInfoService;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    private SpuInfoDescDao descDao;

    @Autowired
    private ProductAttrValueService attrValueService;

    @Autowired
    private ProductAttrValueDao attrValueDao;
    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo querySpuInfoById(Long catId, QueryCondition condition) {

        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        if (catId==null){
            return null;
        }
        if (catId !=0) {
            wrapper.eq("catalog_id",catId);
        }

        String key = condition.getKey();

        if(StringUtils.isNotBlank(key)) {
            wrapper.and(t->t.eq("id",key).or().like("spu_name",key));
        }
        IPage<SpuInfoEntity> page = this.page(PageMaker.getPage(condition), wrapper);
        return new PageVo(page);
    }

    @Override
    public void bigSave(SpuInfoVO spuInfoVO) {
        //1.保存spu相关3张表
        //1.2 保存spu_info
        spuInfoVO.setCreateTime(new Date());
        spuInfoVO.setUodateTime(spuInfoVO.getCreateTime());
        this.save(spuInfoVO);
        Long spuId = spuInfoVO.getId();

        //1.2 保存spu_info_desc
        List<String> spuImages = spuInfoVO.getSpuImages();
        if(!CollectionUtils.isEmpty(spuImages)){
            SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
            spuInfoDescEntity.setSpuId(spuId);
            spuInfoDescEntity.setDecript(StringUtils.join(spuImages,","));
            descDao.insert(spuInfoDescEntity);
        }

        //1.3 保存product_attr_value
        List<BaseAttrVO> baseAttrs = spuInfoVO.getBaseAttrs();
        if(!CollectionUtils.isEmpty(baseAttrs)){
           /* List<ProductAttrValueEntity> attrValueEntities = baseAttrs.stream().map(baseAttrVO ->{
                ProductAttrValueEntity attrValueEntity = baseAttrVO;
                attrValueEntity.setSpuId(spuId);
                return attrValueEntity;
            }).collect(Collectors.toList());
            attrValueService.saveBatch(attrValueEntities);*/
        baseAttrs.forEach(baseAttr->{
                baseAttr.setSpuId(spuId);
                this.attrValueDao.insert(baseAttr);});
        }

        //保存sku相关的3张表
        //保持sms信息的3张表
    }

}