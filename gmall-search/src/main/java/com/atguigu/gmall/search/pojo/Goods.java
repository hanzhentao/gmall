package com.atguigu.gmall.search.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;
import java.util.List;

@Document(indexName = "goods",type = "info", shards = 3, replicas = 2)
@Data
public class Goods {
    @Id
    private Long skuId;
    @Field(type = FieldType.Keyword, index = false)
    private String pic;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Double)
    private Double price;
    @Field(type = FieldType.Long)
    private Long sale; //销量
    @Field(type = FieldType.Boolean)
    private Boolean store;
    @Field(type = FieldType.Date)
    private Date createTime; //新品
    @Field(type = FieldType.Long)
    private Long barndId;
    @Field(type = FieldType.Keyword)
    private String brandName;
    @Field(type = FieldType.Long)
    private Long categoryId;
    @Field(type = FieldType.Keyword)
    private String categoryName;
    @Field(type = FieldType.Nested)
    private List<SearchAttr> attrs;
}
