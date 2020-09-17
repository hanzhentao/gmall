package com.atguigu.gmall.pms.service.impl;

import com.atguigu.core.bean.PageMaker;
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



@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

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

        if(StringUtils.isNotBlank(condition.getKey())) {
            wrapper.and(t->t.eq("id",key).or().like("spu_name",key));
        }
        IPage<SpuInfoEntity> page = this.page(PageMaker.getPage(condition), wrapper);
        return new PageVo(page);
    }

}