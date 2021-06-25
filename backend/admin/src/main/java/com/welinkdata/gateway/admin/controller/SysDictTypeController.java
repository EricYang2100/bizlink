package com.welinkdata.gateway.admin.controller;

import com.welinkdata.gateway.admin.service.SysDictTypeService;
import com.welinkdata.gateway.common.annotation.Log;
import com.welinkdata.gateway.common.constant.UserConstants;
import com.welinkdata.gateway.common.core.controller.BaseController;
import com.welinkdata.gateway.common.core.domain.AjaxResult;
import com.welinkdata.gateway.common.core.domain.entity.SysDictType;
import com.welinkdata.gateway.common.core.page.TableDataInfo;
import com.welinkdata.gateway.common.enums.BusinessType;
import com.welinkdata.gateway.common.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 数据字典
 */
@RestController
@RequestMapping("/system/dict/type")
public class SysDictTypeController extends BaseController {
    @Autowired
    private SysDictTypeService dictTypeService;


    @GetMapping("/list")
    public TableDataInfo list(SysDictType dictType)
    {

        PageRequest pageable = getPageable();
        Page page = dictTypeService.selectDictTypeList(dictType,pageable);
        return getTableDataInfo(page);


    }

    /**
     * 查询字典类型详细
     */
    @GetMapping(value = "/{dictId}")
    public AjaxResult getInfo(@PathVariable String dictId)
    {
        return AjaxResult.success(dictTypeService.selectDictTypeById(dictId));
    }
    /**
     * 新增字典类型
     */
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictType dict)
    {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict)))
        {
            return AjaxResult.error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        //dict.setCreateBy(SecurityUtils.getUsername());
        return toAjax(dictTypeService.insertDictType(dict));
    }


    /**
     * 修改字典类型
     */
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictType dict)
    {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict)))
        {
            return AjaxResult.error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
      //  dict.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(dictTypeService.updateDictType(dict));
    }

    /**
     * 删除字典类型
     */
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public AjaxResult remove(@PathVariable String[] dictIds)
    {
        dictTypeService.deleteDictTypeByIds(dictIds);
        return AjaxResult.success();
    }

    /**
     * 清空缓存
     */
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clearCache")
    public AjaxResult clearCache()
    {
        DictUtils.clearDictCache();
        return AjaxResult.success();
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return AjaxResult.success(dictTypes);
    }

}
