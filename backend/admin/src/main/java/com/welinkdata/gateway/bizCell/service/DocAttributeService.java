package com.welinkdata.gateway.bizCell.service;


import com.welinkdata.gateway.bizCell.domain.DocAttribute;
import com.welinkdata.gateway.bizCell.repository.DocAttributeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocAttributeService {

    private static final Logger log = LoggerFactory.getLogger(DocAttributeService.class);

    @Autowired
    DocAttributeRepository docAttributeRepository;


    /**
     * 通过ID 查询SysDictType
     * @param id id
     * @return 结果
     */
    public DocAttribute selectDocAttributeById(String id){
        return docAttributeRepository.findById(id).orElse(null);
    }


    /**
     * 通过ID 查询SysDictType
     * @param path path
     * @return 结果
     */
    public DocAttribute selectDocAttributeByPath(String path){

        return docAttributeRepository.findFirstByDocPath(path);
    }

    /**
     * 新增保存字典类型信息
     *
     * @param docTreeItem 字典类型信息
     * @return 结果
     */
    public DocAttribute insertDocAttribute(DocAttribute docTreeItem){

        DocAttribute s= docAttributeRepository.insert(docTreeItem);

        // DictUtils.clearDictCache();

        return s;
    }

    /**
     * 修改保存字典类型信息
     *
     * @param docTreeItem 字典类型信息
     * @return 结果
     */
    public DocAttribute updateDocAttribute(DocAttribute docTreeItem){

        DocAttribute newDict = docAttributeRepository.save(docTreeItem);


        return newDict;
    }

}
