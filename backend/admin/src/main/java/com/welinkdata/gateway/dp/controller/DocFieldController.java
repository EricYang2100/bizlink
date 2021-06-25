package com.welinkdata.gateway.dp.controller;

import com.welinkdata.gateway.common.core.controller.BaseController;
import com.welinkdata.gateway.common.core.domain.AjaxResult;
import com.welinkdata.gateway.dp.domain.DocField;
import com.welinkdata.gateway.dp.service.DocFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dp/docField")
public class DocFieldController extends BaseController {

    @Autowired
    private DocFieldService docFieldService;

    /**
     * 查询文档字段列表
     */

    @GetMapping("/list")
    public AjaxResult list(DocField item)
    {
        List<DocField> list = docFieldService.selectDocFieldList(item);
        return AjaxResult.success(list);
    }


    /**
     * 获取文档字段详细信息
     */

    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") String id)
    {
        return AjaxResult.success(docFieldService.selectDocFieldById(id));
    }

    /**
     * 新增文档字段
     */

    @PostMapping
    public AjaxResult add(@RequestBody DocField docField)
    {
        docFieldService.insertDocField(docField);

        return AjaxResult.success();
    }

    /**
     * 修改文档字段
     */

    @PutMapping
    public AjaxResult edit(@RequestBody DocField docField)
    {
        docFieldService.updateDocField(docField);
        return AjaxResult.success();
    }

    /**
     * 删除文档字段
     */

    @DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable String[] categoryIds)
    {
        docFieldService.deleteDocFieldByIds(categoryIds);

        return AjaxResult.success();
    }
}
