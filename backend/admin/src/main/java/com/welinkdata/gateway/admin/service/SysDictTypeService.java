package com.welinkdata.gateway.admin.service;

import com.welinkdata.gateway.admin.domain.SysConfig;
import com.welinkdata.gateway.admin.repository.SysDictDataRepository;
import com.welinkdata.gateway.admin.repository.SysDictTypeRepository;
import com.welinkdata.gateway.common.constant.UserConstants;
import com.welinkdata.gateway.common.core.domain.entity.SysDictData;
import com.welinkdata.gateway.common.core.domain.entity.SysDictType;
import com.welinkdata.gateway.common.utils.DictUtils;
import com.welinkdata.gateway.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

@Service
public class SysDictTypeService {

    private static final Logger log = LoggerFactory.getLogger(SysDictTypeService.class);

    @Autowired
    SysDictDataRepository sysDictDataRepository;
    @Autowired
    SysDictTypeRepository sysDictTypeRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init()
    {
        List<SysDictType> dictTypeList =selectDictTypeAll();
        for (SysDictType dictType : dictTypeList)
        {
            List<SysDictData> dictDatas = sysDictDataRepository.findByDictType(dictType.getDictType());
            DictUtils.setDictCache(dictType.getDictType(), dictDatas);
        }
    }

    /**
     * 获得数据字典类型列表
     * @return 结果
     */
    public List<SysDictType> selectDictTypeAll(){
        return sysDictTypeRepository.findAll();
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    public List<SysDictData> selectDictDataByType(String dictType)
    {
        List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);
        if (StringUtils.isNotEmpty(dictDatas))
        {
            return dictDatas;
        }
        dictDatas = sysDictDataRepository.findByDictType(dictType);
        if (StringUtils.isNotEmpty(dictDatas))
        {
            DictUtils.setDictCache(dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */

    public Page selectDictTypeList(SysDictType dictType, PageRequest pageable)
    {
        Query query = new Query();
        if(StringUtils.isNotEmpty(dictType.getDictType())) {
            query.addCriteria(Criteria.where("dictType").regex(".*"+dictType.getDictType()+".*"));
        }

        if(StringUtils.isNotEmpty(dictType.getDictName())) {
            query.addCriteria(Criteria.where("dictName").regex(".*"+dictType.getDictName()+".*"));
        }

        if(StringUtils.isNotEmpty(dictType.getStatus())) {
            query.addCriteria(Criteria.where("status").is(dictType.getStatus()));
        }



        if(StringUtils.isNotNull(dictType.getParams().get("beginTime"))) {
            Date beginTime=new Date(0);
            Date endTime=new Date();
            try {
                beginTime = new SimpleDateFormat("yyyy-MM-dd")
                        .parse((String) dictType.getParams().get("beginTime"));
                endTime = new SimpleDateFormat("yyyy-MM-dd")
                        .parse((String) dictType.getParams().get("endTime"));

            }catch (Exception e){
                log.error("date format error",e);
            }
            query.addCriteria(Criteria.where("createTime")
                    .gte(beginTime)
                    .lte(endTime));
        }

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));


        query.with(Sort.by(orders));
        int count = (int) mongoTemplate.count(query, SysDictType.class);


        query.with(pageable);
        List<SysDictType> result = mongoTemplate.find(query,SysDictType.class);

        Page page = new PageImpl(result, pageable,  count);

        return page;

    }

    /**
     * 通过ID 查询SysDictType
     * @param id id
     * @return 结果
     */
    public SysDictType selectDictTypeById(String id){
        return sysDictTypeRepository.findById(id).orElse(null);
    }

    /**
     * 新增保存字典类型信息
     *
     * @param sysDictType 字典类型信息
     * @return 结果
     */
    public SysDictType insertDictType(SysDictType sysDictType){
        SysDictType s=sysDictTypeRepository.insert(sysDictType);

        DictUtils.clearDictCache();

        return s;
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    public SysDictType updateDictType(SysDictType dictType){
        SysDictType oldDict = sysDictTypeRepository.findById(dictType.getId()).get();
        if(! oldDict.getDictType().equals(dictType.getDictType())){

            Query query = new Query(Criteria.where("dictType").is(oldDict.getDictType()));
            Update update = new Update();
            update.set("dictType",dictType.getDictType());
            mongoTemplate.updateMulti(query,update, SysDictData.class);


        }
        SysDictType newDict =sysDictTypeRepository.save(dictType);

        DictUtils.clearDictCache();

        return newDict;
    }

    /**
     * 删除字典类型信息--物理删除
     * @param ids 需删除IDs
     */
    public void deleteDictTypeByIds(String[] ids){
        for(String id:ids) {
            SysDictType oldDict = sysDictTypeRepository.findById(id).get();
            sysDictTypeRepository.deleteById(id);
            sysDictDataRepository.deleteSysDictDataByDictType(oldDict.getDictType());
        }
        DictUtils.clearDictCache();
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dictType 字典类型
     * @return 结果
     */
    public String checkDictTypeUnique(SysDictType  dictType){
        SysDictType s = sysDictTypeRepository.findFirstByDictType(dictType.getDictType());
        if( s != null)
            if(! s.getId().equals(dictType.getId())){
                return UserConstants.NOT_UNIQUE;
            }
        return UserConstants.UNIQUE;
    }
}
