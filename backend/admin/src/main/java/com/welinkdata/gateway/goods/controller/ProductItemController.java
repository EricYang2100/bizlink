package com.welinkdata.gateway.goods.controller;


import com.welinkdata.gateway.common.core.controller.BaseController;
import com.welinkdata.gateway.common.core.domain.AjaxResult;
import com.welinkdata.gateway.goods.domain.ProductItem;
import com.welinkdata.gateway.goods.service.ProductItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods/productItem")
public class ProductItemController extends BaseController {


    @Autowired
    private ProductItemService productItemService;

    /**
     * 查询文档字段列表
     */

    @GetMapping("/list")
    public AjaxResult list(ProductItem item)
    {

        List<Map> list = productItemService.selectItemAll(item);
        return AjaxResult.success(list);
    }


    /**
     * 获取文档字段详细信息
     */

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(productItemService.getItemById(id));
    }

    /**
     * 新增文档字段
     */

    @PostMapping
    public AjaxResult add(@RequestBody ProductItem item)
    {
        productItemService.insertItem(item);

        return AjaxResult.success();
    }

    /**
     * 修改文档字段
     */

    @PutMapping
    public AjaxResult edit(@RequestBody ProductItem item)
    {
        productItemService.updateItem(item);
        return AjaxResult.success();
    }

    /**
     * 删除文档字段
     */

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        productItemService.deleteItemByIds(ids);

        return AjaxResult.success();
    }
}
