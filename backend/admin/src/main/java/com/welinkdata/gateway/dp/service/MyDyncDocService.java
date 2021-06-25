package com.welinkdata.gateway.dp.service;


import com.mongodb.client.result.DeleteResult;
import com.welinkdata.gateway.dp.domain.MyDyncDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyDyncDocService {

    private static final Logger log = LoggerFactory.getLogger(MyDyncDocService.class);


    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 搜索列表
     * @param doc 搜索条件
     * @return  结果列表
     */
    public List<MyDyncDoc>  selectDocList(MyDyncDoc doc){
        Query query = new Query();
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();


        Example<MyDyncDoc> example = Example.of(doc,matcher);
        Criteria criteria = Criteria.byExample(example);
        query.addCriteria(criteria);
//        if(! StringUtil.isEmpty(doc.getName())) {
//            Criteria criteria = Criteria.where("name").is(doc.getName());
//            query.addCriteria(criteria);
//        }
//
//
//
//        //sort
//        query.with(Sort.by(new Sort.Order(Sort.Direction.ASC,"name")));

        //page:query.with(Pageable.unpaged()).

        List<MyDyncDoc> list = mongoTemplate.find(query,MyDyncDoc.class);
        return list;
    }

    /**
     * 根据ID查询信息
     *
     * @param id ID
     * @return 结果
     */

    public MyDyncDoc getDocById(String id){
        MyDyncDoc doc = mongoTemplate.findById(id,MyDyncDoc.class);
        return  doc;
    }

    /**
     * 删除信息
     *
     * @param ids 需要删除的ID 数组
     * @return 结果
     */

    public void deleteDocByIds(String[] ids)
    {
        for(String id:ids){
            DeleteResult ds = mongoTemplate.remove(id);
        }

    }


    /**
     * 新增信息
     *
     * @param doc 字典数据信息
     * @return 结果
     */

    public MyDyncDoc insertDoc(MyDyncDoc doc)
    {
        MyDyncDoc newDoc = mongoTemplate.insert(doc);
        return newDoc;
    }

    /**
     * 修改保存字典数据信息
     *
     * @param doc 字典数据信息
     * @return 结果
     */

    public MyDyncDoc updateDoc(MyDyncDoc doc)
    {

        MyDyncDoc newDoc = mongoTemplate.save(doc);
        return newDoc;
    }

}
