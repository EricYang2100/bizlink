package com.welinkdata.gateway.system.service;

import com.welinkdata.gateway.system.domain.SysLoginInfo;
import com.welinkdata.gateway.system.repository.SysLoginInfoRepository;
import com.welinkdata.gateway.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SysLoginInfoService {

    private static final Logger log = LoggerFactory.getLogger(SysLoginInfoService.class);

    @Autowired
    SysLoginInfoRepository sysLoginInfoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 新增系统登录日志
     *
     * @param loginInfo 访问日志对象
     */
    public void insertLoginInfo(SysLoginInfo loginInfo){

        sysLoginInfoRepository.insert(loginInfo);
    }


    /**
     * 查询系统登录日志集合
     *
     * @param loginInfo 访问日志对象
     * @return 登录记录集合
     */
    public Page selectLoginInfoList(SysLoginInfo loginInfo, PageRequest pageable) {

        Query query = new Query();
        if(StringUtils.isNotEmpty(loginInfo.getIpaddr())) {
            query.addCriteria(Criteria.where("ipaddr").regex(".*"+loginInfo.getIpaddr()+".*"));
        }
        if(StringUtils.isNotEmpty(loginInfo.getUserName())) {
            query.addCriteria(Criteria.where("userName").is(loginInfo.getUserName()));
        }

        if(StringUtils.isNotNull(loginInfo.getStatus())) {
            query.addCriteria(Criteria.where("status").is(loginInfo.getStatus()));
        }

        if(StringUtils.isNotNull(loginInfo.getParams().get("beginTime"))) {
            Date beginTime=new Date(0);
            Date endTime=new Date();
            try {
                beginTime = new SimpleDateFormat("yyyy-MM-dd")
                        .parse((String) loginInfo.getParams().get("beginTime"));
                endTime = new SimpleDateFormat("yyyy-MM-dd")
                        .parse((String) loginInfo.getParams().get("endTime"));

            }catch (Exception e){
                log.error("date format error",e);
            }
            query.addCriteria(Criteria.where("loginTime")
                    .gte(beginTime)
                    .lte(endTime));
        }

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"loginTime"));


        query.with(Sort.by(orders));
        int count = (int) mongoTemplate.count(query, SysLoginInfo.class);


        query.with(pageable);
        List<SysLoginInfo> result = mongoTemplate.find(query,SysLoginInfo.class);

        Page page = new PageImpl(result, pageable,  count);

        return page;

    }
//    List<SysLoginInfo> selectLoginInfoList(SysLoginInfo loginInfo)
//    {
//        return sysLoginInfoRepository.findAll();
//    }

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return
     */

    public int deleteLoginInfoByIds(String[] infoIds){
        int count = 0;
        for(String infoId:infoIds){
            sysLoginInfoRepository.deleteById(infoId);
            count++;
        }
        return count;
    }

    /**
     * 清空系统登录日志
     */
    public void cleanLoginInfo(){

    }


}
