package com.welinkdata.gateway.bizDoc.controller;


import com.welinkdata.gateway.common.core.controller.BaseController;
import com.welinkdata.gateway.common.core.domain.AjaxResult;
import com.welinkdata.gateway.bizDoc.domain.MyDyncDoc;
import com.welinkdata.gateway.bizDoc.service.MyDyncDocService;
import com.welinkdata.gateway.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bizDoc/myDyncDoc")
public class MyDyncDocController extends BaseController {



    @Autowired
    private MyDyncDocService dyncDocService;

    /**
     * 查询文档字段列表
     */

    @GetMapping("/list")
    public TableDataInfo list(MyDyncDoc doc)
    {

        PageRequest pageable = getPageable();
        Page page = dyncDocService.selectDocList(doc,pageable);
        return getTableDataInfo(page);


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
