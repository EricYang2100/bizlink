package com.welinkdata.gateway.bizTP.service;


import com.welinkdata.gateway.bizTP.domain.TPInfo;
import com.welinkdata.gateway.bizTP.domain.TPType;
import com.welinkdata.gateway.bizTP.repository.TPInfoRepository;

import com.welinkdata.gateway.bizTP.repository.TPTypeRepository;
import com.welinkdata.gateway.common.core.domain.entity.SysDept;
import com.welinkdata.gateway.common.core.domain.entity.SysUser;
import com.welinkdata.gateway.common.utils.StringUtils;
import com.welinkdata.gateway.system.domain.SysConfig;
import com.welinkdata.gateway.system.repository.SysDeptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


import java.util.ArrayList;

import java.util.List;


@Service
public class TPInfoService {

    private static final Logger log = LoggerFactory.getLogger(TPInfoService.class);

    @Autowired
    TPInfoRepository infoRepository;

    @Autowired
    TPTypeRepository typeRepository;

    @Autowired
    SysDeptRepository deptRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 根据条件分页查询分类
     *
     * @param info 分类信息
     * @return 集合信息
     */
    public Page selectInfoAll(TPInfo info, PageRequest pageable) {
        Query query = new Query();
        if(StringUtils.isNotEmpty(info.getTpName())) {
            query.addCriteria(Criteria.where("tpName").regex(".*"+info.getTpName()+".*"));
        }
        if(StringUtils.isNotEmpty(info.getTpTypeId())) {
            query.addCriteria(Criteria.where("tpTypeFullPath").regex(".*"+info.getTpTypeId()+".*"));
        }
        if(StringUtils.isNotEmpty(info.getDeptId())) {
            query.addCriteria(Criteria.where("deptFullPath").regex(".*"+info.getDeptId()+".*"));
        }
        if(StringUtils.isNotEmpty(info.getCity())) {
            query.addCriteria(Criteria.where("city").regex(info.getCity()+".*"));
        }
        if(StringUtils.isNotNull(info.getTpMgr())) {
            if( info.getTpMgr().length>0) {
                query.addCriteria(Criteria.where("tpMgr").is(info.getTpMgr()[0]));
            }
        }


        if(StringUtils.isNotEmpty(info.getStatus())) {
            query.addCriteria(Criteria.where("status").is(info.getStatus()));
        }

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));

        query.with(Sort.by(orders));
        int count = (int) mongoTemplate.count(query, TPInfo.class);


        query.with(pageable);
        List<TPInfo> result = mongoTemplate.find(query,TPInfo.class);

        Page page = new PageImpl(result, pageable,  count);

        return page;
    }
//    public List<TPInfo> selectInfoAll(TPInfo info)
//    {
//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withMatcher("tpName", startsWith())
//                .withMatcher("tpTypeFullPath", startsWith())
//                .withMatcher("status",   exact())
//                ;
//
//        List<Sort.Order> orders = new ArrayList<>();
//        orders.add(new Sort.Order(Sort.Direction.ASC,"updateTime"));
//
//
//
//        Example<TPInfo> example = Example.of(info,matcher);
//
//        List<TPInfo> pc = infoRepository.findAll(example,Sort.by(orders));
//        return pc;
//    }

    /**
     * 通过ID 查询
     * @param id id
     * @return 结果
     */
    public TPInfo selectInfoById(String id){
        return infoRepository.findById(id).orElse(null);
    }



    /**
     * 新增合作伙伴信息
     *
     * @param info 合作伙伴信息
     * @return 结果
     */
    public TPInfo insertInfo(TPInfo info){
        info.setInviteStatus("0");      //待邀请
        setTPTypeFullPath(info);
        setTPDeptFullPath(info);
        TPInfo s = infoRepository.save(info);

        return s;
    }

    /**
     * 冗余typeFullPath=deptAncestor+","+typeId
     * @param info
     */
    private void setTPTypeFullPath(TPInfo info) {
        if( StringUtils.isNotEmpty(info.getTpTypeId())){
            TPType dept = typeRepository.findById(info.getTpTypeId()).orElse(null);
            if(StringUtils.isNotNull(dept)){
                info.setTpTypeFullPath(dept.getAncestors()+","+info.getTpTypeId());
            }
        }
    }

    /**
     * 冗余DeptFull=deptAncestor+","+deptId
     * @param info
     */
    private void setTPDeptFullPath(TPInfo info) {
        if( StringUtils.isNotEmpty(info.getTpTypeId())){
            SysDept dept = deptRepository.findById(info.getDeptId()).orElse(null);
            if(StringUtils.isNotNull(dept)){
                info.setDeptFullPath(dept.getAncestors()+","+info.getDeptId());
            }
        }
    }


    /**
     * 修改保存信息
     * 判断parentId 是否改变，如改变递归更新fullPath
     * @param info 分类信息
     * @return 结果
     */
    public TPInfo updateInfo(TPInfo info){
            setTPTypeFullPath(info);
            setTPDeptFullPath(info);
            return infoRepository.save(info);
    }


    /**
     * 删除--物理删除
     * @param ids 需删除IDs     *
     */
    public void deleteInfoByIds(String[] ids){
        for(String id:ids) {

            infoRepository.deleteById(id);

        }

    }


}
