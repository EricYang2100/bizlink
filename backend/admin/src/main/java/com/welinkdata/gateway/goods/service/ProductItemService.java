package com.welinkdata.gateway.goods.service;

import com.mongodb.client.result.DeleteResult;
import com.welinkdata.gateway.dp.domain.MyDyncDoc;
import com.welinkdata.gateway.dp.service.MyDyncDocService;
import com.welinkdata.gateway.goods.domain.ProductCategory;
import com.welinkdata.gateway.goods.domain.ProductItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import org.bson.Document;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ProductItemService {

    private static final Logger log = LoggerFactory.getLogger(ProductItemService.class);


    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Map> selectItemAll(ProductItem item){

    //todo:调整分类的祖级编码，转换为全路径编码， 产品通过记录全路径编码， 支持按目录搜索和关联分类
   //lookup 解决$_id 转换问题
        Aggregation agg = Aggregation.newAggregation(l -> new Document("$lookup",
                        new Document("from", mongoTemplate.getCollectionName(ProductCategory.class))
                                .append("let", new Document("categoryId", new Document("$toObjectId", "$categoryId")))
//                                .append("let", new Document("itemId", "id"))
                                .append("pipeline",
                                        Arrays.asList(new Document("$match",
                                                new Document("$expr",
                                                        new Document("$eq", Arrays.asList("$_id", "$$categoryId"))))))
                                .append("as", "category")),
                Aggregation.unwind("category", Boolean.TRUE));

        AggregationResults<Map> results = mongoTemplate.aggregate(agg,
                mongoTemplate.getCollectionName(ProductItem.class), Map.class);


        List<Map> l =  results.getMappedResults();
        for( Map m:l){
            Object o = m.get("_id");
            m.put("id",o.toString());
        }

        return l;


//  标准lookup ，但存在$id ObjectId 与String 不一致的问题。
//        LookupOperation lookupOperation=LookupOperation.newLookup().
//                from("bizdoc_product_category").  //关联从表名
//                localField("categoryId").     //主表关联字段categoryId
//                foreignField("_id").//从表关联的字段
//                as("category");   //查询结果名
//
//        Aggregation aggregation=Aggregation.newAggregation(lookupOperation);
//        List<Map> results = mongoTemplate.aggregate(aggregation,ProductItem.class, Map.class).getMappedResults();
//        //上面的student必须是查询的主表名
//        log.debug("selectItemAll result:",results);
//        return results;


    }

    /**
     * 根据ID查询信息
     *
     * @param id ID
     * @return 结果
     */

    public ProductItem getItemById(String id){
        ProductItem doc = mongoTemplate.findById(id,ProductItem.class);
        return  doc;
    }

    /**
     * 删除信息
     *
     * @param ids 需要删除的ID 数组
     * @return 结果
     */

    public void deleteItemByIds(String[] ids)
    {
        for(String id:ids){
            Query query = new Query();
            Criteria criteria = Criteria.where("id").is(id);
            query.addCriteria(criteria);

            DeleteResult ds = mongoTemplate.remove(query,ProductItem.class);
        }

    }


    /**
     * 新增信息
     *
     * @param doc 字典数据信息
     * @return 结果
     */

    public ProductItem insertItem(ProductItem doc)
    {
        ProductItem newDoc = mongoTemplate.insert(doc);
        return newDoc;
    }

    /**
     * 修改保存字典数据信息
     *
     * @param doc 字典数据信息
     * @return 结果
     */

    public ProductItem updateItem(ProductItem doc)
    {

        ProductItem newDoc = mongoTemplate.save(doc);
        return newDoc;
    }
}
