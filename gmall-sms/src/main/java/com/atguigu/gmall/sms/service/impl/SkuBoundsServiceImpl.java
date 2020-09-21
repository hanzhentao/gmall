package com.atguigu.gmall.sms.service.impl;

import com.atguigu.gmall.sms.dao.SkuFullReductionDao;
import com.atguigu.gmall.sms.dao.SkuLadderDao;
import com.atguigu.gmall.sms.entity.SkuFullReductionEntity;
import com.atguigu.gmall.sms.entity.SkuLadderEntity;
import com.atguigu.gmall.sms.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.sms.dao.SkuBoundsDao;
import com.atguigu.gmall.sms.entity.SkuBoundsEntity;
import com.atguigu.gmall.sms.service.SkuBoundsService;
import vo.SkuSaleVO;


@Service("skuBoundsService")
public class SkuBoundsServiceImpl extends ServiceImpl<SkuBoundsDao, SkuBoundsEntity> implements SkuBoundsService {
    @Autowired
    private SkuLadderDao skuLadderDao;

    @Autowired
    private SkuFullReductionDao reductionDao;
    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SkuBoundsEntity> page = this.page(
                new Query<SkuBoundsEntity>().getPage(params),
                new QueryWrapper<SkuBoundsEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public void saveSaleAttr(SkuSaleVO skuSaleVO) {
        Long skuId = skuSaleVO.getSkuId();
        //保存sms_sku.bounds
        SkuBoundsEntity skuBoundsEntity = new SkuBoundsEntity();
        skuBoundsEntity.setSkuId(skuId);
        skuBoundsEntity.setGrowBounds(skuSaleVO.getGrowBounds());
        skuBoundsEntity.setBuyBounds(skuSaleVO.getBuyBounds());
        List<Integer> work = skuSaleVO.getWork();
        skuBoundsEntity.setWork(work.get(3)*1+work.get(2)*2+work.get(1)*4+work.get(0)*8);
        this.save(skuBoundsEntity);

        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuId);
        skuLadderEntity.setDiscount(skuSaleVO.getDiscount());
        skuLadderEntity.setAddOther(skuSaleVO.getLadderaddOther());
        this.skuLadderDao.insert(skuLadderEntity);

        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        skuFullReductionEntity.setId(skuId);
        skuFullReductionEntity.setFullPrice(skuSaleVO.getFullPrice());
        skuFullReductionEntity.setReducePrice(skuSaleVO.getReducePrice());
        skuFullReductionEntity.setAddOther(skuSaleVO.getFulladdOther());
        reductionDao.insert(skuFullReductionEntity);
    }

}