package com.welinkdata.gateway.system.service;

import com.welinkdata.gateway.system.repository.SysDictDataRepository;

import com.welinkdata.gateway.common.core.domain.entity.SysDictData;
import com.welinkdata.gateway.common.utils.DictUtils;
import com.welinkdata.gateway.common.utils.StringUtils;
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
public class SysDictDataService {

    private static final Logger log = LoggerFactory.getLogger(SysDictTypeService.class);

    @Autowired
    SysDictDataRepository sysDictDataRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息     */

//    public List<SysDictData> selectDictDataList(SysDictData dictData)
//    {
//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withMatcher("dictType", exact())
//                .withMatcher("status",  exact())
//                .withMatcher("dictLabel",exact())
//                .withMatcher("dictValue",exact())
//                .withMatcher("dictSort",ignoreCase())
//                ;
//
//        Example<SysDictData> example = Example.of(dictData,matcher);
//
//        return sysDictDataRepository.findAll(example,Sort.by(new Sort.Order(Sort.Direction.ASC,"dictSort")));
//    }
    public Page selectDictDataList(SysDictData dictData, PageRequest pageable)
    {
        Query query = new Query();
        if(StringUtils.isNotEmpty(dictData.getDictType())) {
            query.addCriteria(Criteria.where("dictType").is(dictData.getDictType()));
        }

        if(StringUtils.isNotEmpty(dictData.getDictLabel())) {
            query.addCriteria(Criteria.where("dictLabel").regex(".*"+dictData.getDictLabel()+".*"));
        }

        if(StringUtils.isNotEmpty(dictData.getStatus())) {
            query.addCriteria(Criteria.where("status").is(dictData.getStatus()));
        }


        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC,"dictSort"));


        query.with(Sort.by(orders));
        int count = (int) mongoTemplate.count(query, SysDictData.class);


        query.with(pageable);
        List<SysDictData> result = mongoTemplate.find(query,SysDictData.class);

        Page page = new PageImpl(result, pageable,  count);

        return page;

    }
    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */

    public String selectDictLabel(String dictType, String dictValue)
    {
        SysDictData dictData = sysDictDataRepository.findByDictTypeAndDictValue(dictType, dictValue);
        if( dictData != null){
            return dictData.getDictLabel();
        }
        return "";
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param id 字典数据ID
     * @return 字典数据
     */

    public SysDictData selectDictDataById(String id)
    {

        return sysDictDataRepository.findById(id).get();
    }

    /**
     * 删除字典数据信息
     *
     * @param ids 需要删除的字典数据ID 数组
     * @return 结果
     */

    public void deleteDictDataByIds(String[] ids)
    {
        for(String id:ids)
            sysDictDataRepository.deleteById(id);

        DictUtils.clearDictCache();

    }


    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */

    public SysDictData insertDictData(SysDictData dictData)
    {
        SysDictData newData =  sysDictDataRepository.insert(dictData);

        DictUtils.clearDictCache();

        return newData;
    }

    /**
     * 修改保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */

    public SysDictData updateDictData(SysDictData dictData)
    {
        SysDictData newData =sysDictDataRepository.save(dictData);
        DictUtils.clearDictCache();

        return newData;

    }

}
