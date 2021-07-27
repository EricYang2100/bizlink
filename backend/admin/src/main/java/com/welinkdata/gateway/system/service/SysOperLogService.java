package com.welinkdata.gateway.system.service;

import com.welinkdata.gateway.system.domain.SysOperLog;
import com.welinkdata.gateway.system.repository.SysOperLogRepository;
import com.welinkdata.gateway.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SysOperLogService {

    private static final Logger log = LoggerFactory.getLogger(SysOperLogService.class);

    @Autowired
    SysOperLogRepository sysOperLogRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */

    public void insertOperlog(SysOperLog operLog) {
        operLog.setOperTime(new Date(System.currentTimeMillis()));
        sysOperLogRepository.insert(operLog);
    }


    /**
     * 查询系统操作日志集合
     *
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */

    public Page selectOperLogList(SysOperLog operLog, PageRequest pageable) {

//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withMatcher("title", exact())          //系统模块
//                .withMatcher("operName", exact())       //操作人员
//                .withMatcher("businessTypes", exact())       //业务类型
//                .withMatcher("status",   exact())           //状态
//                ;
//        Example<SysOperLog> example = Example.of(operLog,matcher);

        Query query = new Query();
        if(StringUtils.isNotEmpty(operLog.getTitle())) {
            query.addCriteria(Criteria.where("title").is(operLog.getTitle()));
        }
        if(StringUtils.isNotEmpty(operLog.getOperName())) {
            query.addCriteria(Criteria.where("operName").is(operLog.getOperName()));
        }
        if(StringUtils.isNotNull(operLog.getBusinessType())) {
            query.addCriteria(Criteria.where("businessType").is(operLog.getBusinessType()));
        }
        if(StringUtils.isNotNull(operLog.getStatus())) {
            query.addCriteria(Criteria.where("status").is(operLog.getStatus()));
        }

        if(StringUtils.isNotNull(operLog.getParams().get("beginTime"))) {
            Date beginTime=new Date(0);
            Date endTime=new Date();
            try {
                 beginTime = new SimpleDateFormat("yyyy-MM-dd")
                        .parse((String) operLog.getParams().get("beginTime"));
                 endTime = new SimpleDateFormat("yyyy-MM-dd")
                        .parse((String) operLog.getParams().get("endTime"));

            }catch (Exception e){
                log.error("date format error",e);
            }
            query.addCriteria(Criteria.where("operTime")
                    .gte(beginTime)
                    .lte(endTime));
        }

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"operTime"));


        query.with(Sort.by(orders));
        int count = (int) mongoTemplate.count(query, SysOperLog.class);


        query.with(pageable);
        List<SysOperLog> result = mongoTemplate.find(query,SysOperLog.class);

        Page page = new PageImpl(result, pageable,  count);

        return page;

    }

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */

    public int deleteOperLogByIds(String[] operIds){
        int count = 0;
        for( String operId:operIds){
            sysOperLogRepository.deleteById(operId);
            count++;
        }
        return count;
    }

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    public SysOperLog selectOperLogById(String operId){
        return sysOperLogRepository.findById(operId).orElse(null);
    }

    /**
     * 清空操作日志
     */
    public void cleanOperLog(){
        sysOperLogRepository.deleteAll();
    }
}