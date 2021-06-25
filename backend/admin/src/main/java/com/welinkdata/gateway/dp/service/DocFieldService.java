package com.welinkdata.gateway.dp.service;

import com.welinkdata.gateway.common.utils.StringUtils;
import com.welinkdata.gateway.dp.domain.DocField;
import com.welinkdata.gateway.dp.repository.DocFieldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

@Service
public class DocFieldService {

    private static final Logger log = LoggerFactory.getLogger(DocFieldService.class);

    @Autowired
    DocFieldRepository docFieldRepository;

//    @Autowired
//    private MongoTemplate mongoTemplate;


    /**
     * 根据路径查询文档字段设置
     * todo: 启用cache
     * @param docPath 模板名称
     * @return 模板数据集合信息
     */
    public List<DocField> getDocFieldList(String docPath)
    {
//        List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);
//        if (StringUtils.isNotEmpty(dictDatas))
//        {
//            return dictDatas;
//        }
        List<DocField> dictDatas = docFieldRepository.findByDocPath(docPath);
        if (StringUtils.isNotEmpty(dictDatas))
        {
            // DictUtils.setDictCache(dictType, dictDatas);

        }
        return dictDatas;
    }
    /**
     * 根据条件分页查询字典数据
     *
     * @param docField 字典数据信息
     * @return 字典数据集合信息     */

    public List<DocField> selectDocFieldList(DocField docField)
    {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("sysName", startsWith())
                .withMatcher("fieldName",startsWith())
                .withMatcher("orderNum",ignoreCase())
                ;

        Example<DocField> example = Example.of(docField,matcher);

        return docFieldRepository.findAll(example, Sort.by(new Sort.Order(Sort.Direction.ASC,"orderNum")));
    }


    /**
     * 根据ID查询信息
     *
     * @param id ID
     * @return 结果
     */

    public DocField selectDocFieldById(String id)
    {

        return docFieldRepository.findById(id).get();
    }

    /**
     * 删除信息
     *
     * @param ids 需要删除的ID 数组
     * @return 结果
     */

    public void deleteDocFieldByIds(String[] ids)
    {
        for(String id:ids)
            docFieldRepository.deleteById(id);

     //   DictUtils.clearDictCache();

    }


    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */

    public DocField insertDocField(DocField dictData)
    {
        DocField newData =  docFieldRepository.insert(dictData);

   //     DictUtils.clearDictCache();

        return newData;
    }

    /**
     * 修改保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */

    public DocField updateDocField(DocField dictData)
    {
        DocField newData = docFieldRepository.save(dictData);
 //       DictUtils.clearDictCache();

        return newData;

    }
}
