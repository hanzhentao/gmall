package com.atguigu.gmall.pms.service.impl;

import com.atguigu.core.bean.PageMaker;
import com.atguigu.gmall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gmall.pms.dao.AttrDao;
import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.vo.GroupVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.AttrGroupDao;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.gmall.pms.service.AttrGroupService;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    private AttrAttrgroupRelationDao relationDao;

    @Autowired
    private AttrGroupDao groupDao;

    @Autowired
    private AttrDao attrDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryGroupByPage(QueryCondition condition, Long catId) {
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();

        if (catId != null) {
            wrapper.eq("catelog_id", catId);
        }

        IPage<AttrGroupEntity> group = this.page(PageMaker.getPage(condition),wrapper);
        return new PageVo(group);
    }

    @Override
    public GroupVO queryGroupWithAttrByGid(Long gid) {
        //查询group
        GroupVO groupVO = new GroupVO();
        AttrGroupEntity groupEntity = this.groupDao.selectById(gid);
        BeanUtils.copyProperties(groupEntity,groupVO);

        //根据gid查询关联关系,获取attrId
        List<AttrAttrgroupRelationEntity> relations = this.relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", gid));
        if(CollectionUtils.isEmpty(relations)) return groupVO;
        groupVO.setRelations(relations);
        List<Long> attrIds = relations.stream().map(r->r.getAttrId()).collect(Collectors.toList());

        List<AttrEntity> attrEntities = this.attrDao.selectBatchIds(attrIds);
        groupVO.setAttrEntities(attrEntities);
        return groupVO;
    }

    @Override
    public List<GroupVO> queryGroupWithAttrByCid(Long catId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("catelog_id", catId);
        Collection<AttrGroupEntity> attrGroupEntities = this.listByMap(map);
        GroupVO groupVO = new GroupVO();
        List<GroupVO> collect = attrGroupEntities.stream().map(attrGroupEntity ->
                queryGroupWithAttrByGid(attrGroupEntity.getAttrGroupId())
        ).collect(Collectors.toList());

        return collect;
    }

}