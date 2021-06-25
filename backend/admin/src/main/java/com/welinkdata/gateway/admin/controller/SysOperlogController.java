package com.welinkdata.gateway.admin.controller;

import com.welinkdata.gateway.admin.domain.SysOperLog;
import com.welinkdata.gateway.admin.service.SysOperLogService;
import com.welinkdata.gateway.common.annotation.Log;
import com.welinkdata.gateway.common.core.controller.BaseController;
import com.welinkdata.gateway.common.core.domain.AjaxResult;
import com.welinkdata.gateway.common.core.page.TableDataInfo;
import com.welinkdata.gateway.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController
{
    @Autowired
    private SysOperLogService operLogService;

    //@PreAuthorize("@ss.hasPermi('monitor:operlog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysOperLog operLog)
    {
        PageRequest pageable = getPageable();
        Page page = operLogService.selectOperLogList(operLog,pageable);
        return getTableDataInfo(page);
    }

//    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
//    @PreAuthorize("@ss.hasPermi('monitor:operlog:export')")
//    @GetMapping("/export")
//    public AjaxResult export(SysOperLog operLog)
//    {
//        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
//        ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
//        return util.exportExcel(list, "操作日志");
//    }

    //@PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/{operIds}")
    public AjaxResult remove(@PathVariable String[] operIds)
    {
        return toAjax(operLogService.deleteOperLogByIds(operIds));
    }

    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
   // @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/clean")
    public AjaxResult clean()
    {
        operLogService.cleanOperLog();
        return AjaxResult.success();
    }

}
