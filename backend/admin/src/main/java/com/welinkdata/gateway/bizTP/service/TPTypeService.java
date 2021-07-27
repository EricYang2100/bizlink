package com.welinkdata.gateway.bizTP.service;


import com.welinkdata.gateway.bizTP.domain.TPInfo;
import com.welinkdata.gateway.bizTP.domain.TPType;
import com.welinkdata.gateway.bizTP.repository.TPInfoRepository;
import com.welinkdata.gateway.bizTP.repository.TPTypeRepository;
import com.welinkdata.gateway.common.core.domain.entity.SysDept;
import com.welinkdata.gateway.common.core.domain.entity.SysUser;
import com.welinkdata.gateway.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

@Service
public class TPTypeService {

    private static final Logger log = LoggerFactory.getLogger(TPTypeService.class);

    @Autowired
    TPTypeRepository typeRepository;

    @Autowired
    TPInfoRepository infoRepository;

    /**
     * 获得产品分类列表
     * @return 结果
     */
    public List<TPType> selectCTypeAll(){
        return typeRepository.findAll();
    }


    /**
     * 根据条件分页查询分类
     *
     * @param type 分类信息
     * @return 分类集合信息
     */

    public List<TPType> selectTypeAll(TPType type)
    {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("typeName", startsWith())
                .withMatcher("status",   exact())
                ;

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC,"parentId"));
        orders.add(new Sort.Order(Sort.Direction.ASC,"orderNum"));


        Example<TPType> example = Example.of(type,matcher);

        List<TPType> pc = typeRepository.findAll(example,Sort.by(orders));
        return pc;
    }

    /**
     * 通过ID 查询
     * @param id id
     * @return 结果
     */
    public TPType selectTypeById(String id){
        return typeRepository.findById(id).orElse(null);
    }



    /**
     * 新增保存分类信息
     *
     * @param type 分类信息
     * @return 结果
     */
    public TPType insertType(TPType type){

        TPType info = typeRepository.findById(type.getParentId()).orElse(null);
        if( StringUtils.isNotNull(info)) {

            String ancestors = info.getAncestors();
            if(ancestors == null ){
                type.setAncestors(type.getParentId());
            }else {
                type.setAncestors(info.getAncestors() + "," + type.getParentId());
            }
        } else {
            type.setAncestors("0");
        }


        return typeRepository.insert(type);


    }

    /**
     * 修改保存信息
     * 判断parentId 是否改变，如改变递归更新fullPath
     * @param type 分类信息
     * @return 结果
     */
    public TPType updateType(TPType type){


        TPType oldDept = typeRepository.findById(type.getId()).orElse(null);

        if( !oldDept.getParentId().equals(type.getParentId())){  // parentId 改变

            TPType newParentDept = null;
            String newAncestors = "";

            if(! "0".equals(type.getParentId())) {
                newParentDept = typeRepository.findById(type.getParentId()).orElse(null);
            }

            if (StringUtils.isNotNull(newParentDept) )
            {
                newAncestors = newParentDept.getAncestors() + "," + newParentDept.getId();
            }

            type.setAncestors(newAncestors);
            TPType result = typeRepository.save(type);
            updateTPInfo(type.getId(), newAncestors);
            updateChildren(type.getId(), newAncestors);
            return result;
        } else {
            TPType result = typeRepository.save(type);
            return result;
        }



    }

    /**
     * 递归更新子节点的父级
     * todo:更新TPInfo 表冗余的Ancestors
     * @param id
     * @param newAncestors
     *
     */
    private void updateChildren(String id, String newAncestors) {
        String childAncestors = newAncestors+ ","+ id;
        List<TPType> list = typeRepository.findAllByParentId(id);

        for( TPType item : list){
            item.setAncestors(childAncestors);
            typeRepository.save(item);
            updateTPInfo(id, newAncestors);
            updateChildren(item.getId(),childAncestors);

        }
    }

    /**
     * 更新TPInfo，冗余的TpTypeFullPath
     * @param id
     * @param newAncestors
     */
    private void updateTPInfo(String id, String newAncestors) {
        List<TPInfo> list =  infoRepository.findAllByTpTypeId(id);
        for(TPInfo user: list){
            user.setTpTypeFullPath(newAncestors+","+id);
            infoRepository.save(user);
        }
    }

    /**
     * 删除字典类型信息--物理删除
     * @param ids 需删除IDs
     * todo:判断子节点是否为空
     */
    public void deleteTypeByIds(String[] ids){
        for(String id:ids) {

            typeRepository.deleteById(id);

        }
        //DictUtils.clearDictCache();
    }


}