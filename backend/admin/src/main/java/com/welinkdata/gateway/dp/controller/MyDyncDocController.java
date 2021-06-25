package com.welinkdata.gateway.dp.controller;


import com.welinkdata.gateway.common.core.controller.BaseController;
import com.welinkdata.gateway.common.core.domain.AjaxResult;
import com.welinkdata.gateway.dp.domain.MyDyncDoc;
import com.welinkdata.gateway.dp.service.MyDyncDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dp/myDyncDoc")
public class MyDyncDocController extends BaseController {



    @Autowired
    private MyDyncDocService dyncDocService;

    /**
     * 查询文档字段列表
     */

    @GetMapping("/list")
    public AjaxResult list(MyDyncDoc doc)
    {

        List<MyDyncDoc> list = dyncDocService.selectDocList(doc);
        return AjaxResult.success(list);
    }


    /**
     * 获取文档字段详细信息
     */

    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") String id)
    {
        return AjaxResult.success(dyncDocService.getDocById(id));
    }

    /**
     * 新增文档字段
     */

    @PostMapping
    public AjaxResult add(@RequestBody MyDyncDoc docField)
    {
        dyncDocService.insertDoc(docField);

        return AjaxResult.success();
    }

    /**
     * 修改文档字段
     */

    @PutMapping
    public AjaxResult edit(@RequestBody MyDyncDoc docField)
    {
        dyncDocService.updateDoc(docField);
        return AjaxResult.success();
    }

    /**
     * 删除文档字段
     */

    @DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable String[] categoryIds)
    {
        dyncDocService.deleteDocByIds(categoryIds);

        return AjaxResult.success();
    }
}
