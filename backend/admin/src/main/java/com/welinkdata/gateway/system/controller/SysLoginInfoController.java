package com.welinkdata.gateway.system.controller;

import com.welinkdata.gateway.system.domain.SysLoginInfo;
import com.welinkdata.gateway.system.service.SysLoginInfoService;
import com.welinkdata.gateway.common.annotation.Log;
import com.welinkdata.gateway.common.core.controller.BaseController;
import com.welinkdata.gateway.common.core.domain.AjaxResult;
import com.welinkdata.gateway.common.core.page.TableDataInfo;
import com.welinkdata.gateway.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monitor/logininfor")
public class SysLoginInfoController extends BaseController
{
    @Autowired
    private SysLoginInfoService logininforService;

    //@PreAuthorize("@ss.hasPermi('monitor:logininfor:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLoginInfo logininfor)
    {
        PageRequest pageable = getPageable();
        Page page = logininforService.selectLoginInfoList(logininfor,pageable);
        return getTableDataInfo(page);

    }

//    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
//    @PreAuthorize("@ss.hasPermi('monitor:logininfor:export')")
//    @GetMapping("/export")
//    public AjaxResult export(SysLoginInfo logininfor)
//    {
//        List<SysLoginInfo> list = logininforService.selectLogininforList(logininfor);
//        ExcelUtil<SysLoginInfo> util = new ExcelUtil<SysLogininfor>(SysLogininfor.class);
//        return util.exportExcel(list, "登录日志");
//    }

    //@PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable String[] infoIds)
    {
        return toAjax(logininforService.deleteLoginInfoByIds(infoIds));
    }

   // @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean()
    {
        logininforService.cleanLoginInfo();
        return AjaxResult.success();
    }
}
