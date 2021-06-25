package com.welinkdata.gateway.admin.controller;

import com.welinkdata.gateway.admin.service.SysMenuService;
import com.welinkdata.gateway.admin.service.SysRoleService;
import com.welinkdata.gateway.common.annotation.Log;
import com.welinkdata.gateway.common.constant.Constants;
import com.welinkdata.gateway.common.constant.UserConstants;
import com.welinkdata.gateway.common.core.controller.BaseController;
import com.welinkdata.gateway.common.core.domain.AjaxResult;
import com.welinkdata.gateway.common.core.domain.entity.SysMenu;
import com.welinkdata.gateway.common.core.domain.model.LoginUser;
import com.welinkdata.gateway.common.enums.BusinessType;
import com.welinkdata.gateway.common.utils.ServletUtils;
import com.welinkdata.gateway.common.utils.StringUtils;
import com.welinkdata.gateway.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单信息
 *
 * @author Eric
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {

    @Autowired
    private SysMenuService menuService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取菜单列表
     * todo: limit loginUser
     */
    //@PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
//        Long userId = loginUser.getUser().getUserId();
//        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        List<SysMenu> menus = menuService.selectSysMenuAll(menu);
        return AjaxResult.success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable String menuId)
    {
        return AjaxResult.success(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     * todo
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysMenu menu)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        //Long userId = loginUser.getUser().getUserId();
        //List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        List<SysMenu> menus = menuService.selectSysMenuAll(menu);
        return AjaxResult.success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     * todo:implement
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeSelect(@PathVariable("roleId") String roleId)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        AjaxResult ajax = AjaxResult.success();
        List<SysMenu> menus = menuService.selectMenuList(loginUser.getUser().getUserId());

        ajax.put("checkedKeys", roleService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));

        return ajax;
    }

    /**
     * 新增菜单
     */
    //@PreAuthorize("@ss.hasPermi('system:menu:add')")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenu menu)
    {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu)))
        {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame())
                && !StringUtils.startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS))
        {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        //menu.setCreateBy(SecurityUtils.getUsername());
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
   // @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysMenu menu)
    {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu)))
        {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame())
                && !StringUtils.startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS))
        {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        else if (menu.getMenuId().equals(menu.getParentId()))
        {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
       // menu.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     */
  //  @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") String menuId)
    {
        if (menuService.hasChildByMenuId(menuId))
        {
            return AjaxResult.error("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId))
        {
            return AjaxResult.error("菜单已分配,不允许删除");
        }
        menuService.deleteMenuById(menuId);
        return AjaxResult.success();
    }

}
