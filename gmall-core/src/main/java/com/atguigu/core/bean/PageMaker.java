package com.atguigu.core.bean;

import com.atguigu.core.utils.SQLFilter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;

public class PageMaker{
    public static<T> IPage<T> getPage(QueryCondition param){
        return getPage(param,null,false);
    }

    public static<T> IPage<T> getPage(QueryCondition param,String defaultOrderField,boolean isAsc){
        long curPage = 1;
        long limit = 10;

        if (param.getPage() != null) {
            curPage=param.getPage();
        }
        if (param.getLimit() != null) {
            limit=param.getLimit();
        }
        Page<T> page = new Page<T>(curPage, limit);

        String orderField = SQLFilter.sqlInject(param.getSidx());
        String order = param.getOrder();

        if(StringUtils.isNotEmpty(orderField) && StringUtils.isNotEmpty(order)){
            if("asc".equalsIgnoreCase(order)){
                return page.setAsc(order);
            }else {
                return page.setDesc(order);
            }
        }

        if(isAsc) page.setAsc(defaultOrderField);
        else page.setDesc(defaultOrderField);

        return page;
    }

}
