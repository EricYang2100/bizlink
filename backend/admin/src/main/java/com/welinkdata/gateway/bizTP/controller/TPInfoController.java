package com.welinkdata.gateway.bizTP.controller;


import com.welinkdata.gateway.bizTP.domain.TPInfo;
import com.welinkdata.gateway.bizTP.domain.TPType;
import com.welinkdata.gateway.bizTP.service.TPInfoService;
import com.welinkdata.gateway.common.core.controller.BaseController;
import com.welinkdata.gateway.common.core.domain.AjaxResult;
import com.welinkdata.gateway.common.core.page.TableDataInfo;
import com.welinkdata.gateway.system.domain.SysConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bizTP/tpInfo")
public class TPInfoController extends BaseController {

    @Autowired
    private TPInfoService infoService;


    /**
     * 获取列表
     */

    @GetMapping("/list")
    public TableDataInfo list(TPInfo info)
    {


        PageRequest pageable = getPageable();
        Page page = infoService.selectInfoAll(info,pageable);
        return getTableDataInfo(page);
    }


    /**
     * 获取商品分类详细信息
     */

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(infoService.selectInfoById(id));
    }

    /**
     * 新增商品分类
     */

    @PostMapping
    public AjaxResult add(@RequestBody TPInfo info)
    {
        infoService.insertInfo(info);

        return AjaxResult.success();
    }

    /**
     * 修改商品分类
     */

    @PutMapping
    public AjaxResult edit(@RequestBody TPInfo info)
    {
        infoService.updateInfo(info);
        return AjaxResult.success();
    }

    /**
     * 删除商品分类
     */

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {


        infoService.deleteInfoByIds(ids);

        return AjaxResult.success();
    }
}
