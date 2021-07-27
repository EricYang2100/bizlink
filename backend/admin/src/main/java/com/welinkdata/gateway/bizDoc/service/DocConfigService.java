package com.welinkdata.gateway.bizDoc.service;


import com.welinkdata.gateway.bizDoc.domain.DocConfig;
import com.welinkdata.gateway.bizDoc.repository.DocConfigRepository;
import com.welinkdata.gateway.common.constant.Constants;
import com.welinkdata.gateway.common.constant.UserConstants;
import com.welinkdata.gateway.common.exception.CustomException;
import com.welinkdata.gateway.common.utils.StringUtils;
import com.welinkdata.gateway.system.domain.SysConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class DocConfigService {

    private static final Logger log = LoggerFactory.getLogger(MyDyncDocService.class);


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DocConfigRepository docConfigRepository;

    /**
     * 查询文档配置信息
     *
     * @param docSysName 配置名称
     * @return 文档配置信息
     */
    public DocConfig selectConfigByName(String docSysName){
        return docConfigRepository.findByDocSysName(docSysName);
    }

    /**
     * 查询文档配置信息
     *
     * @param id 配置ID
     * @return 文档配置信息
     */
    public DocConfig selectConfigById(String id){
        return docConfigRepository.findById(id).orElse(null);
    }
    /**
     * 按类型查询文档配置
     *
     * @param shareType 文档共享类型
     * @return 文档配置信息列表
     */
    public List<DocConfig> selectConfigList(String shareType) {
        return docConfigRepository.findAllByShareType(shareType);
    }

    /**
     * 新增文档配置
     *
     * @param config 文档配置信息
     * @return 结果
     */

    public DocConfig insertConfig(DocConfig config){
        DocConfig sysConfig =  docConfigRepository.insert(config);
        return sysConfig;
    }

    /**
     * 修改文档配置
     *
     * @param config 文档配置信息
     * @return 结果
     */

    public DocConfig updateConfig(DocConfig config){
        DocConfig sysConfig =  docConfigRepository.save(config);

        return sysConfig;
    }

    /**
     * 删除文档信息
     *
     * @param configId 需要删除的参数ID
     * @return 结果
     */

    public void deleteConfigById(String configId){

        docConfigRepository.deleteById(configId);

    }


    /**
     * 校验文档键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */

    public String checkSysNameUnique(DocConfig config){

        DocConfig info = docConfigRepository.findByDocSysName(config.getDocSysName());
        if (StringUtils.isNotNull(info) && (!info.getId().equals(config.getId())))
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
