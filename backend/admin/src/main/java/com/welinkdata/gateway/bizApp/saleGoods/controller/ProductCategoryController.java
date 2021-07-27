package com.welinkdata.gateway.bizApp.saleGoods.controller;

import com.welinkdata.gateway.common.core.controller.BaseController;
import com.welinkdata.gateway.common.core.domain.AjaxResult;
import com.welinkdata.gateway.bizApp.saleGoods.domain.ProductCategory;
import com.welinkdata.gateway.bizApp.saleGoods.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods/productCategory")
public class ProductCategoryController extends BaseController {

    @Autowired
    private ProductCategoryService productService;

    /**
     * 查询商品分类列表
     */

    @GetMapping("/list")
    public AjaxResult list(ProductCategory goodsCategory)
    {
        List<ProductCategory> list = productService.selectCategoryAll(goodsCategory);
        return AjaxResult.success(list);
    }


    /**
     * 获取商品分类详细信息
     */

    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") String categoryId)
    {
        return AjaxResult.success(productService.selectCategoryById(categoryId));
    }

    /**
     * 新增商品分类
     */

    @PostMapping
    public AjaxResult add(@RequestBody ProductCategory goodsCategory)
    {
        productService.insertCategory(goodsCategory);

        return AjaxResult.success();
    }

    /**
     * 修改商品分类
     */

    @PutMapping
    public AjaxResult edit(@RequestBody ProductCategory goodsCategory)
    {
        productService.updateCategory(goodsCategory);
        return AjaxResult.success();
    }

    /**
     * 删除商品分类
     */

    @DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable String[] categoryIds)
    {


        productService.deleteCategoryByIds(categoryIds);

        return AjaxResult.success();
    }
}
