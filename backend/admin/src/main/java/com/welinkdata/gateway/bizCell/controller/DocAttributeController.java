package com.welinkdata.gateway.bizCell.controller;


import com.welinkdata.gateway.bizCell.domain.DocAttribute;
import com.welinkdata.gateway.bizCell.service.DocAttributeService;
import com.welinkdata.gateway.common.annotation.Log;
import com.welinkdata.gateway.common.constant.UserConstants;
import com.welinkdata.gateway.common.core.controller.BaseController;
import com.welinkdata.gateway.common.core.domain.AjaxResult;
import com.welinkdata.gateway.common.core.domain.entity.SysDept;
import com.welinkdata.gateway.common.enums.BusinessType;
import com.welinkdata.gateway.common.utils.SecurityUtils;
import com.welinkdata.gateway.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bizCell/docAttribute")
public class DocAttributeController extends BaseController {

    @Autowired
    private DocAttributeService docAttributeService;


    /**
     * 获取文档属性的详细信息
     * todo:check DocTreeItem 存在定义
     */

    @GetMapping(value = "/{docPath}")
    public AjaxResult getInfo(@PathVariable("docPath") String docPath)
    {
        DocAttribute attribute = docAttributeService.selectDocAttributeByPath(docPath);
        if(StringUtils.isNull(attribute)) {
            attribute = new DocAttribute();
            attribute.setDocPath(docPath);
            attribute.setMaxId(1L);
            attribute = docAttributeService.insertDocAttribute(attribute);
        }

        return AjaxResult.success(attribute);
    }
    /**
     * 修改文档文档属性
     */

    @PutMapping
    public AjaxResult edit(@RequestBody DocAttribute goodsCategory)
    {
        docAttributeService.updateDocAttribute(goodsCategory);
        return AjaxResult.success();
    }


}
