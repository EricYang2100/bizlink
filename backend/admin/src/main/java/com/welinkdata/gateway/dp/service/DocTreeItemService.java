package com.welinkdata.gateway.dp.service;

import com.mongodb.DBObject;
import com.welinkdata.gateway.dp.domain.DocTreeItem;

import com.welinkdata.gateway.dp.repository.DocTreeItemRepository;
import com.welinkdata.gateway.common.constant.UserConstants;
import com.welinkdata.gateway.common.utils.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

@Service
public class DocTreeItemService {

    private static final Logger log = LoggerFactory.getLogger(DocTreeItemService.class);
    public static final String PATH_SPEC_CHAR = ".";

    @Autowired
    DocTreeItemRepository docTreeItemRepository;

    @Autowired
    private MongoTemplate mongoTemplate;



    /**
     * todo:项目启动时，初始化文档定义到Redis缓存
     */
//    @PostConstruct
//    public void init()
//    {
//        List<SysDictType> dictTypeList =selectDictTypeAll();
//        for (SysDictType dictType : dictTypeList)
//        {
//            List<SysDictData> dictDatas = sysDictDataRepository.findByDictType(dictType.getDictType());
//            DictUtils.setDictCache(dictType.getDictType(), dictDatas);
//        }
//    }



    /**
     * 根据条件分页查询字典类型
     *
     * @param docTreeItem 字典类型信息
     * @return 字典类型集合信息
     */

    public List<DocTreeItem> selectDocTreeItemList(DocTreeItem docTreeItem)
    {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", startsWith())
                .withMatcher("code",   startsWith())
                .withMatcher("docType",   exact())
                .withMatcher("status",   exact())
                ;

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC,"parentId"));
        orders.add(new Sort.Order(Sort.Direction.ASC,"orderNum"));



        Example<DocTreeItem> example = Example.of(docTreeItem,matcher);

        return docTreeItemRepository.findAll(example);
    }

    /**
     * 通过ID 查询SysDictType
     * @param id id
     * @return 结果
     */
    public DocTreeItem selectDocTreeItemById(String id){
        return docTreeItemRepository.findById(id).orElse(null);
    }


    /**
     * 通过ID 查询SysDictType
     * @param path path
     * @return 结果
     */
    public DocTreeItem selectDocTreeItemByPath(String path){

        return docTreeItemRepository.findFirstByDocPath(path);
    }

    /**
     * 新增保存字典类型信息
     *
     * @param docTreeItem 字典类型信息
     * @return 结果
     */
    public DocTreeItem insertDocTreeItem(DocTreeItem docTreeItem){
        setDocPath(docTreeItem);
        DocTreeItem s= docTreeItemRepository.insert(docTreeItem);

      // DictUtils.clearDictCache();

        return s;
    }

    /**
     * 修改保存字典类型信息
     *
     * @param docTreeItem 字典类型信息
     * @return 结果
     */
    public DocTreeItem updateDocTreeItem(DocTreeItem docTreeItem){
        String s = docTreeItem.getDocPath();
        setDocPath(docTreeItem);
        DocTreeItem newDict = docTreeItemRepository.save(docTreeItem);
        if(! newDict.getDocPath().equals(s)){
            updateChildrenDocPath(newDict);
        }


        return newDict;
    }

    private void updateChildrenDocPath(DocTreeItem parentItem) {
        List <DocTreeItem> docList = docTreeItemRepository.findAllByParentId(parentItem.getId());
        for( DocTreeItem childItem:docList){
            childItem.setDocPath(parentItem.getDocPath()+PATH_SPEC_CHAR+ childItem.getCode());
            docTreeItemRepository.save(childItem);
            updateChildrenDocPath(childItem);       //递归子节点
        }
    }

    /**
     * 删除字典类型信息--物理删除
     * @param ids 需删除IDs
     */
    public void deleteDocTreeItemByIds(String[] ids){
        for(String id:ids) {
            DocTreeItem oldDict = docTreeItemRepository.findById(id).get();
            docTreeItemRepository.deleteById(id);
            // todo:删除只节点和对应明细定义
           // docFieldRepository.deleteDocDefineDetailByTemplateCode(oldDict.getTemplateCode());
        }
        //DictUtils.clearDictCache();
    }

    /**
     * 校验名称称是否唯一
     *
     * @param docTreeItem 字典类型
     * @return 结果
     */
    public String checkCodeUnique(DocTreeItem docTreeItem){
        setDocPath(docTreeItem);
        DocTreeItem s = docTreeItemRepository.findFirstByDocPath(docTreeItem.getDocPath());
        if( s != null)
            if(! s.getId().equals(docTreeItem.getId())){
                return UserConstants.NOT_UNIQUE;
            }
        return UserConstants.UNIQUE;
    }

    private void setDocPath(DocTreeItem docTreeItem) {
        if( ! docTreeItem.getParentId().equals("0")) {
            DocTreeItem parentItem = docTreeItemRepository.findById(docTreeItem.getParentId()).get();
            docTreeItem.setDocPath(parentItem.getDocPath()+PATH_SPEC_CHAR+ docTreeItem.getCode());
        } else {
            docTreeItem.setDocPath(docTreeItem.getCode());
        }
    }
}
