package com.atguigu.gmall.search;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.search.pojo.Goods;
import com.atguigu.gmall.search.feign.GmallPmsClient;
import com.atguigu.gmall.search.feign.GmallWmsClient;
import com.atguigu.gmall.search.pojo.SearchAttr;
import com.atguigu.gmall.search.repository.GoodsRepository;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class GmallSearchApplicationTests {
    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Autowired
    private GmallPmsClient pmsClient;

    @Autowired
    private GmallWmsClient wmsClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    void contextLoads() {
        this.restTemplate.createIndex(Goods.class);
        this.restTemplate.putMapping(Goods.class);
    }

    @Test
    void importData(){

        Long pageNum=1l;
        Long pageSize=100l;
        do {
            //分页查询spu
            QueryCondition queryCondition = new QueryCondition();
            queryCondition.setPage(pageNum);
            queryCondition.setLimit(pageSize);
            Resp<List<SpuInfoEntity>> spusData = this.pmsClient.querySpusByPage(queryCondition);
            List<SpuInfoEntity> spus = spusData.getData();
            if (CollectionUtils.isEmpty(spus)) {
                continue;
            }

            //遍历spu，查询sku
            spus.forEach(spu-> {
                Resp<List<SkuInfoEntity>> skuInfos = this.pmsClient.querySkuInfosBySpuId(spu.getId());
                List<SkuInfoEntity> skuInfosData = skuInfos.getData();
                if(!CollectionUtils.isEmpty(skuInfosData)){
                    List<Goods> goodsList = skuInfosData.stream().map(sku -> {
                        Goods goods = new Goods();
                        Resp<List<ProductAttrValueEntity>> attrValues = this.pmsClient.querySearchAttrValueBySpuId(spu.getId());
                        List<ProductAttrValueEntity> attrValuesData = attrValues.getData();
                        if(!CollectionUtils.isEmpty(attrValuesData)) {
                            List<SearchAttr> searchAttrs = attrValuesData.stream().map(attrValue -> {
                                SearchAttr searchAttr = new SearchAttr();
                                searchAttr.setAttrId(attrValue.getAttrId());
                                searchAttr.setAttrName(attrValue.getAttrName());
                                searchAttr.setAttrValue(attrValue.getAttrValue());
                                return searchAttr;
                            }).collect(Collectors.toList());
                            goods.setAttrs(searchAttrs);
                        }

                        Resp<BrandEntity> brandEntityResp = this.pmsClient.queryBrandById(sku.getBrandId());
                        BrandEntity brandData = brandEntityResp.getData();

                        if(brandData!=null){
                            goods.setBarndId(sku.getBrandId());
                            goods.setBrandName(brandData.getName());
                        }



                        Resp<CategoryEntity> categoryEntityResp = this.pmsClient.queryCategoryById(sku.getCatalogId());
                        CategoryEntity categoryEntity = categoryEntityResp.getData();
                        if (categoryEntity!=null){
                            goods.setCategoryId(sku.getCatalogId());
                            goods.setCategoryName(categoryEntity.getName());
                        }

                        goods.setCreateTime(spu.getCreateTime());
                        goods.setPic(sku.getSkuDefaultImg());
                        goods.setPrice(sku.getPrice().doubleValue());
                        goods.setSale(0l);
                        goods.setSkuId(sku.getSkuId());
                        Resp<List<WareSkuEntity>> wares = this.wmsClient.queryWaresBySkuId(sku.getSkuId());
                        List<WareSkuEntity> waresData = wares.getData();
                        if(!CollectionUtils.isEmpty(waresData)){
                            boolean isAnyStore = waresData.stream().anyMatch(t -> t.getStock() > 0);
                            goods.setStore(isAnyStore);
                        }

                        goods.setTitle(sku.getSkuTitle());
                        return goods;
                    }).collect(Collectors.toList());
                    this.goodsRepository.saveAll(goodsList);
                }

            });
            //把sku转换成goods对象

            //查询brandName categorName by skuId

            //查询store

            //导入索引库
            pageSize=(long) spus.size();
        }while (pageSize == 100l);
    }

    @Test
    void queyTest(){
        System.out.println(this.pmsClient.querySearchAttrValueBySpuId(5l));
    }

}
