package com.welinkdata.gateway.system.controller;

import com.welinkdata.gateway.system.service.SysDictDataService;
import com.welinkdata.gateway.system.service.SysDictTypeService;
import com.welinkdata.gateway.common.annotation.Log;
import com.welinkdata.gateway.common.core.controller.BaseController;
import com.welinkdata.gateway.common.core.domain.AjaxResult;
import com.welinkdata.gateway.common.core.domain.entity.SysDictData;
import com.welinkdata.gateway.common.core.page.TableDataInfo;
import com.welinkdata.gateway.common.enums.BusinessType;
import com.welinkdata.gateway.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典信息
 *
 */

@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {
    @Autowired
    private SysDictDataService dictDataService;

    @Autowired
    private SysDictTypeService dictTypeService;


    @GetMapping("/list")
    public TableDataInfo list(SysDictData dictData)
    {


        PageRequest pageable = getPageable();
        Page page = dictDataService.selectDictDataList(dictData,pageable);
        return getTableDataInfo(page);
    }

    /**
     * 查询字典数据详细
     */
    @GetMapping(value = "/{dictCode}")
    public AjaxResult getInfo(@PathVariable String dictCode)
    {
        return AjaxResult.success(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType)
    {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data))
        {
            data = new ArrayList<SysDictData>();
        }
        return AjaxResult.success(data);
    }

    /**
     * 新增字典类型
     */
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictData dict)
    {
        //dict.setCreateBy(SecurityUtils.getUsername());
        return toAjax(dictDataService.insertDictData(dict));
    }
    /**
     * 修改保存字典类型
     */
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictData dict)
    {
       // dict.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(dictDataService.updateDictData(dict));
    }

    /**
     * 删除字典类型
     */
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public AjaxResult remove(@PathVariable String[] dictCodes)
    {
        dictDataService.deleteDictDataByIds(dictCodes);
        return AjaxResult.success();
    }
}
