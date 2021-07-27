package com.welinkdata.gateway.system.service;


import com.welinkdata.gateway.system.domain.SysConfig;
import com.welinkdata.gateway.system.repository.SysConfigRepository;
import com.welinkdata.gateway.common.constant.Constants;
import com.welinkdata.gateway.common.constant.UserConstants;
import com.welinkdata.gateway.common.core.redis.RedisCache;
import com.welinkdata.gateway.common.core.text.Convert;
import com.welinkdata.gateway.common.exception.CustomException;
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

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class SysConfigService {


    private static final Logger log = LoggerFactory.getLogger(SysConfigService.class);

    @Autowired
    SysConfigRepository sysConfigRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisCache redisCache;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init()
    {
        List<SysConfig> configsList = sysConfigRepository.findAll();
        for (SysConfig config : configsList)
        {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey)
    {
        return Constants.SYS_CONFIG_KEY + configKey;
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    public SysConfig selectConfigById(String configId){
        return sysConfigRepository.findById(configId).orElse(null);
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */

    public String selectConfigByKey(String configKey){
        String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
        if (StringUtils.isNotEmpty(configValue))
        {
            return configValue;
        }

        SysConfig retConfig =  sysConfigRepository.findByConfigKey(configKey);
        if (StringUtils.isNotNull(retConfig))
        {
            redisCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 查询参数配置列表
     * @param config 参数配置信息
     * @return 参数配置集合
     */
//    public List<SysConfig> selectConfigList(SysConfig config) {
//        List<SysConfig> configsList = sysConfigRepository.findAll();
//        return configsList;
//    }
    public Page selectConfigList(SysConfig config, PageRequest pageable) {
        Query query = new Query();
        if(StringUtils.isNotEmpty(config.getConfigName())) {
            query.addCriteria(Criteria.where("configName").regex(".*"+config.getConfigName()+".*"));
        }
        if(StringUtils.isNotEmpty(config.getConfigKey())) {
            query.addCriteria(Criteria.where("configKey").regex(".*"+config.getConfigKey()+".*"));
        }
        if(StringUtils.isNotEmpty(config.getConfigType())) {
            query.addCriteria(Criteria.where("configType").is(config.getConfigType()));
        }


        if(StringUtils.isNotNull(config.getParams().get("beginTime"))) {
            Date beginTime=new Date(0);
            Date endTime=new Date();
            try {
                beginTime = new SimpleDateFormat("yyyy-MM-dd")
                        .parse((String) config.getParams().get("beginTime"));
                endTime = new SimpleDateFormat("yyyy-MM-dd")
                        .parse((String) config.getParams().get("endTime"));

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
        int count = (int) mongoTemplate.count(query, SysConfig.class);


        query.with(pageable);
        List<SysConfig> result = mongoTemplate.find(query,SysConfig.class);

        Page page = new PageImpl(result, pageable,  count);

        return page;
    }
    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */

    public SysConfig insertConfig(SysConfig config){
        SysConfig sysConfig =  sysConfigRepository.insert(config);
        if (StringUtils.isNotNull(sysConfig) )
        {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return sysConfig;
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */

    public SysConfig updateConfig(SysConfig config){
        SysConfig sysConfig =  sysConfigRepository.save(config);
        if (StringUtils.isNotNull(sysConfig) )
        {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return sysConfig;
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     * @return 结果
     */

    public int deleteConfigByIds(String[] configIds){
        int count=0;
        for (String configId : configIds)
        {
            SysConfig config = selectConfigById(configId);
            if (StringUtils.equals(UserConstants.YES, config.getConfigType()))
            {
                throw new CustomException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            sysConfigRepository.deleteById(configId);
            count++;
        }

        if (count > 0)
        {
            Collection<String> keys = redisCache.keys(Constants.SYS_CONFIG_KEY + "*");
            redisCache.deleteObject(keys);
        }
        return count;
    }


    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */

    public String checkConfigKeyUnique(SysConfig config){

        SysConfig info = sysConfigRepository.findByConfigKey(config.getConfigKey());
        if (StringUtils.isNotNull(info) && (!info.getConfigId().equals(config.getConfigId())))
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
    /**
     * 清空缓存数据
     */
    public void clearCache()
    {
        Collection<String> keys = redisCache.keys(Constants.SYS_CONFIG_KEY + "*");
        redisCache.deleteObject(keys);
    }

}
