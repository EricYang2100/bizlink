package com.welinkdata.gateway.bizTP.controller;

import com.welinkdata.gateway.bizTP.domain.TPType;
import com.welinkdata.gateway.bizTP.service.TPTypeService;
import com.welinkdata.gateway.common.core.controller.BaseController;
import com.welinkdata.gateway.common.core.domain.AjaxResult;
import com.welinkdata.gateway.common.core.domain.DictEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/bizTP/tpType")
public class TPTypeController extends BaseController {

    @Autowired
    private TPTypeService typeService;

    /**
     * 查询分类列表
     */

    @GetMapping("/list")
    public AjaxResult list(TPType type)
    {
        List<TPType> list = typeService.selectTypeAll(type);
        return AjaxResult.success(list);
    }


    /**
     * 查询类型数据字典
     */

    @GetMapping("/dictList")
    public AjaxResult dictlist()
    {

        List<TPType> list = typeService.selectCTypeAll();
        ArrayList dictList = new ArrayList();

        for(TPType t:list){
            DictEntity de = new DictEntity(t.getTypeName(),t.getId());
            dictList.add(de);
        }
        return AjaxResult.success(dictList);
    }


    /**
     * 获取商品分类详细信息
     */

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(typeService.selectTypeById(id));
    }

    /**
     * 新增商品分类
     */

    @PostMapping
    public AjaxResult add(@RequestBody TPType type)
    {
        typeService.insertType(type);

        return AjaxResult.success();
    }

    /**
     * 修改商品分类
     */

    @PutMapping
    public AjaxResult edit(@RequestBody TPType type)
    {
        typeService.updateType(type);
        return AjaxResult.success();
    }

    /**
     * 删除商品分类
     */

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {


        typeService.deleteTypeByIds(ids);

        return AjaxResult.success();
    }
}
