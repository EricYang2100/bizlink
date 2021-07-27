package com.welinkdata.gateway.bizDoc.service;


import com.mongodb.client.result.DeleteResult;
import com.welinkdata.gateway.bizCell.domain.DocAttribute;
import com.welinkdata.gateway.bizCell.domain.DocAttributeField;
import com.welinkdata.gateway.bizCell.service.DocAttributeService;
import com.welinkdata.gateway.bizDoc.domain.DocConfig;
import com.welinkdata.gateway.bizDoc.domain.MyDyncDoc;
import com.welinkdata.gateway.common.constant.DocFieldConstants;
import com.welinkdata.gateway.common.exception.CustomException;
import com.welinkdata.gateway.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MyDyncDocService {

    private static final Logger log = LoggerFactory.getLogger(MyDyncDocService.class);


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    DocAttributeService docFieldService;

    @Autowired
    DocConfigService docConfigService;

    /**
     * 搜索列表
     * @param doc 搜索条件
     * @return  结果列表
     */
    public Page  selectDocList(MyDyncDoc doc,PageRequest pageable){

        DocConfig docConfig = docConfigService.selectConfigByName(doc.getDocSysName());
        DocAttribute attribute = docFieldService.selectDocAttributeByPath(docConfig.getDocFieldPath());
        List<DocAttributeField> fieldList =attribute.getFieldList();
        if(StringUtils.isNull(fieldList)){
            throw new CustomException(String.format("文档(%s)未定义 ", doc.getDocSysName()));
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("docSysName").is(doc.getDocSysName()));

        for(DocAttributeField field:fieldList){
            if( "1".equals(field.getIsQuery())) {    // 查询字段
                String sValue = (String)doc.getParams().get(field.getSysName());
                Object value = null;
                if( StringUtils.isEmpty(sValue)) continue;

                if (DocFieldConstants.STRING.equals(field.getDataType())) {
                        value = sValue;
                }
                if (DocFieldConstants.LONG.equals(field.getDataType())) {
                        value = Long.parseLong(sValue);
                }
                if (DocFieldConstants.DOUBLE.equals(field.getDataType())) {
                    value = Double.parseDouble(sValue);
                }
                if (DocFieldConstants.DATE.equals(field.getDataType())) {
                    try {
                        value = SimpleDateFormat.getInstance().parse(sValue);
                    } catch (ParseException e) {
                        log.warn("prase error",e);
                    }
                }

                String dbField = "f." + field.getSysName();
                if (StringUtils.isNotNull(value)) {
                    if ("EQ".equals(field.getQueryType())) {

                        query.addCriteria(Criteria.where(dbField).is(value));
                        continue;
                    }
                    if ("NE".equals(field.getQueryType())) {

                        query.addCriteria(Criteria.where(dbField).is(value).not());
                        continue;
                    }
                    if ("GT".equals(field.getQueryType())) {

                        query.addCriteria(Criteria.where(dbField).gt(value));
                        continue;
                    }
                    if ("GE".equals(field.getQueryType())) {

                        query.addCriteria(Criteria.where(dbField).gte(value));
                        continue;
                    }
                    if ("LT".equals(field.getQueryType())) {

                        query.addCriteria(Criteria.where(dbField).lt(value));
                        continue;
                    }
                    if ("LTE".equals(field.getQueryType())) {

                        query.addCriteria(Criteria.where(dbField).lte(value));
                        continue;
                    }
                    if ("LIKE".equals(field.getQueryType())) {  //限定字符串

                        query.addCriteria(Criteria.where(dbField).regex(value + ".*"));
                        continue;
                    }
                    if ("REGEX".equals(field.getQueryType())) {      //限定字符串

                        query.addCriteria(Criteria.where(dbField).regex(".*" + value + ".*"));
                        continue;
                    }
                }

                if ("BETWEEN".equals(field.getQueryType())) {
                    //todo: 范围查询
                    String beginParam = "begin" + field.getSysName();
                    String endParam = "end"+field.getSysName();
                    if (DocFieldConstants.DATE.equals(field.getDataType())) {
                        Date beginTime=new Date(0);
                        Date endTime=new Date();
                        try {
                            beginTime = new SimpleDateFormat("yyyy-MM-dd")
                                    .parse((String) doc.getParams().get(beginParam));
                            endTime = new SimpleDateFormat("yyyy-MM-dd")
                                    .parse((String) doc.getParams().get(endParam));

                        }catch (Exception e){
                            log.error("date format error",e);
                        }
                        query.addCriteria(Criteria.where(dbField)
                                .gte(beginTime)
                                .lte(endTime));
                    }
                    if (DocFieldConstants.LONG.equals(field.getDataType())) {
                        Long begin=new Long(-99999L);
                        Long end=new Long(99999L);
                        try {
                            begin = Long.parseLong((String) doc.getParams().get(beginParam));
                            end= Long.parseLong((String) doc.getParams().get(endParam));

                        }catch (Exception e){
                            log.error("number format error",e);
                        }
                        query.addCriteria(Criteria.where(dbField)
                                .gte(begin)
                                .lte(end));
                    }
                    if (DocFieldConstants.DOUBLE.equals(field.getDataType())) {
                        Double begin=new Double(-99999L);
                        Double end=new Double(99999L);
                        try {
                            begin = Double.parseDouble((String) doc.getParams().get(beginParam));
                            end = Double.parseDouble((String) doc.getParams().get(endParam));

                        }catch (Exception e){
                            log.error("number format error",e);
                        }
                        query.addCriteria(Criteria.where(dbField)
                                .gte(begin)
                                .lte(end));
                    }
                    //todo:货币
                }
            }
        }


        //todo: 增加排序字段的定义
        //缺省按创建时间排序
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));


        query.with(Sort.by(orders));
        int count = (int) mongoTemplate.count(query, MyDyncDoc.class);


        query.with(pageable);
        List<MyDyncDoc> result = mongoTemplate.find(query,MyDyncDoc.class);

        Page page = new PageImpl(result, pageable,  count);

        return page;


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
     * todo: 数据类型转换
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
     *  todo: 数据类型转换
     * @param doc 字典数据信息
     * @return 结果
     */

    public MyDyncDoc updateDoc(MyDyncDoc doc)
    {

        MyDyncDoc newDoc = mongoTemplate.save(doc);
        return newDoc;
    }

}
