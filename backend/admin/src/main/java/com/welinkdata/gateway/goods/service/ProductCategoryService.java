package com.welinkdata.gateway.goods.service;

import com.welinkdata.gateway.goods.domain.ProductCategory;
import com.welinkdata.gateway.goods.repository.ProductCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

@Service
public class ProductCategoryService {

    private static final Logger log = LoggerFactory.getLogger(ProductCategoryService.class);

    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @Autowired
    private MongoTemplate mongoTemplate;



    /**
     * 获得产品分类列表
     * @return 结果
     */
    public List<ProductCategory> selectCategoryAll(){
        return productCategoryRepository.findAll();
    }


    /**
     * 根据条件分页查询分类
     *
     * @param category 分类信息
     * @return 分类集合信息
     */

    public List<ProductCategory> selectCategoryAll(ProductCategory category)
    {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("categoryName", startsWith())
                .withMatcher("status",   exact())
                ;

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC,"parentId"));
        orders.add(new Sort.Order(Sort.Direction.ASC,"orderNum"));


        Example<ProductCategory> example = Example.of(category,matcher);

        List<ProductCategory> pc = productCategoryRepository.findAll(example,Sort.by(orders));
        return pc;
    }

    /**
     * 通过ID 查询ProductCategory
     * @param id id
     * @return 结果
     */
    public ProductCategory selectCategoryById(String id){
        return productCategoryRepository.findById(id).orElse(null);
    }


    /**
     * 通过ID 查询ProductCategory
     * @param fullPath id
     * @return 结果
     */
    public ProductCategory selectCategoryByFullPath(String fullPath){
        return productCategoryRepository.findFirstByFullPath(fullPath);
    }
    /**
     * 新增保存分类信息
     *
     * @param category 分类信息
     * @return 结果
     */
    public ProductCategory insertCategory(ProductCategory category){

        if(! category.getParentId().equals("0")) {
            //获取父分类信息，更新 组级代码
            ProductCategory parent = productCategoryRepository.findById(category.getParentId()).get();
            if(parent.getFullPath() != null) {
                category.setFullPath(parent.getFullPath() + "-" + category.getId());
            }else {
                category.setFullPath(category.getId());
            }
        } else {
            category.setFullPath(null);
        }
        ProductCategory s=productCategoryRepository.insert(category);

        return s;
    }

    /**
     * 修改保存信息
     *
     * @param category 分类信息
     * @return 结果
     */
    public ProductCategory updateCategory(ProductCategory category){
//        ProductCategory oldDict = productCategoryRepository.findById(category.getId()).get();

        ProductCategory newDict =productCategoryRepository.save(category);



        return newDict;
    }

    /**
     * 删除字典类型信息--物理删除
     * @param ids 需删除IDs
     * todo:判断子节点是否为空
     */
    public void deleteCategoryByIds(String[] ids){
        for(String id:ids) {

            productCategoryRepository.deleteById(id);

        }
        //DictUtils.clearDictCache();
    }


}
