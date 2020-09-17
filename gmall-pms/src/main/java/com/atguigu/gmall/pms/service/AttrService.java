package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.vo.AttrVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 商品属性
 *
 * @author hanzhentao
 * @email panbubblepanbubble@163.com
 * @date 2020-09-13 12:00:53
 */
public interface AttrService extends IService<AttrEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo queryDescByType(QueryCondition condition, Long cid, Integer type);

    void saveAttr(AttrVO attrVO);
}

