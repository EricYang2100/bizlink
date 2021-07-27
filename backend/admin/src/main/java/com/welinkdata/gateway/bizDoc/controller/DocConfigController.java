package com.welinkdata.gateway.bizDoc.controller;

import com.welinkdata.gateway.bizDoc.domain.DocConfig;

import com.welinkdata.gateway.bizDoc.service.DocConfigService;

import com.welinkdata.gateway.common.core.controller.BaseController;
import com.welinkdata.gateway.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bizDoc/docConfig")
public class DocConfigController extends BaseController {


    @Autowired
    private DocConfigService docConfigService;

    /**
     * 查询文档字段列表
     */

    @GetMapping("/list")
    public AjaxResult list(DocConfig config)
    {
        return  AjaxResult.success(docConfigService.selectConfigList(config.getShareType()));
    }


    /**
     * 获取文档字段详细信息
     */

    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") String id)
    {
        return AjaxResult.success(docConfigService.selectConfigById(id));
    }

    /**
     * 新增文档字段
     */

    @PostMapping
    public AjaxResult add(@RequestBody DocConfig config)
    {
        docConfigService.insertConfig(config);

        return AjaxResult.success();
    }

    /**
     * 修改文档字段
     */

    @PutMapping
    public AjaxResult edit(@RequestBody DocConfig docField)
    {
        docConfigService.updateConfig(docField);
        return AjaxResult.success();
    }

    /**
     * 删除文档字段
     */

    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable String id)
    {
        docConfigService.deleteConfigById(id);

        return AjaxResult.success();
    }

}
