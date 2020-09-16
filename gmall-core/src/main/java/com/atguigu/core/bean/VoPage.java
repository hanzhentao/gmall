package com.atguigu.core.bean;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

@Data
public class VoPage {
    /**
     * 列表数据
     */
    private List<?> list;
    /**
     * 总记录数
     */
    private int totalCount;
    /**
     * 每页记录数
     */
    private int pageSize;
    /**
     * 当前页数
     */
    private int curPage;

    /**
     * 总页数
     */
    private int totalPage;

    public VoPage(List<?> list,int totalCount,int pageSize,int curPage){
        this.list = list;
        this.totalCount=totalCount;
        this.curPage=curPage;
        this.pageSize=pageSize;
        this.totalPage= (int) Math.ceil((double) totalCount/pageSize);
    }

    public VoPage(IPage<?> page){
        this.list=page.getRecords();
        this.curPage= (int) page.getCurrent();
        this.totalCount= (int) page.getTotal();
        this.totalPage= (int) page.getPages();
        this.pageSize= (int) page.getSize();
    }

}
