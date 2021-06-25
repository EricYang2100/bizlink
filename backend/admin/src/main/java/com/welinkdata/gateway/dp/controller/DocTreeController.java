package com.welinkdata.gateway.dp.controller;


import com.welinkdata.gateway.common.core.controller.BaseController;
import com.welinkdata.gateway.common.core.domain.AjaxResult;
import com.welinkdata.gateway.dp.domain.DocTreeItem;
import com.welinkdata.gateway.dp.service.DocTreeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dp/docTreeItem")
public class DocTreeController extends BaseController {

    @Autowired
    private DocTreeItemService docTreeItemService;

    /**
     * 查询文档处理列表
     */

    @GetMapping("/list")
    public AjaxResult list(DocTreeItem item)
    {
        List<DocTreeItem> list = docTreeItemService.selectDocTreeItemList(item);
        return AjaxResult.success(list);
    }

    /**
     * 使用文档路径，获取文档item的详细信息
     */

    @GetMapping(value = "/pathSelect/{path}")
    public AjaxResult getDocTreeItem(@PathVariable("path") String path)
    {
        return AjaxResult.success(docTreeItemService.selectDocTreeItemByPath(path));
    }

    /**
     * 获取文档item的详细信息
     */

    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") String categoryId)
    {
        return AjaxResult.success(docTreeItemService.selectDocTreeItemById(categoryId));
    }

    /**
     * 新增文档处理子项
     */

    @PostMapping
    public AjaxResult add(@RequestBody DocTreeItem goodsCategory)
    {
        docTreeItemService.insertDocTreeItem(goodsCategory);

        return AjaxResult.success();
    }

    /**
     * 修改文档处理子项
     */

    @PutMapping
    public AjaxResult edit(@RequestBody DocTreeItem goodsCategory)
    {
        docTreeItemService.updateDocTreeItem(goodsCategory);
        return AjaxResult.success();
    }

    /**
     * 删除文档处理子项
     */

    @DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable String[] categoryIds)
    {


        docTreeItemService.deleteDocTreeItemByIds(categoryIds);

        return AjaxResult.success();
    }
}
