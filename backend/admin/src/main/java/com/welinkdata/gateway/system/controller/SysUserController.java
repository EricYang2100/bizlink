package com.welinkdata.gateway.system.controller;


import com.welinkdata.gateway.common.core.domain.DictEntity;
import com.welinkdata.gateway.common.core.domain.entity.SysDept;
import com.welinkdata.gateway.system.service.SysPostService;
import com.welinkdata.gateway.system.service.SysRoleService;
import com.welinkdata.gateway.system.service.SysUserService;
import com.welinkdata.gateway.common.annotation.Log;
import com.welinkdata.gateway.common.constant.UserConstants;
import com.welinkdata.gateway.common.core.controller.BaseController;
import com.welinkdata.gateway.common.core.domain.AjaxResult;
import com.welinkdata.gateway.common.core.domain.entity.SysRole;
import com.welinkdata.gateway.common.core.domain.entity.SysUser;
import com.welinkdata.gateway.common.core.page.TableDataInfo;
import com.welinkdata.gateway.common.enums.BusinessType;
import com.welinkdata.gateway.common.utils.SecurityUtils;
import com.welinkdata.gateway.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息
 *
 * @author Eric Yang
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController
{
    @Autowired
    SysUserService userService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysPostService postService;

    /**
     * 获取用户列表
     */
    //@PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user)
    {
//        startPage();
//        List<SysUser> list = userService.selectUserList(user);
//        return getDataTable(list);

        PageRequest pageable = getPageable();
        Page page = userService.selectUserList(user,pageable);
        return getTableDataInfo(page);
    }

    /**
     * 查询用户数据字典
     */
    @GetMapping("/dictList")
    public AjaxResult userDictlist()
    {
        SysUser user = new SysUser();
        List<SysUser> list = userService.selectUserAll(user);
        ArrayList dictList = new ArrayList();

        for(SysUser t:list){
            DictEntity de = new DictEntity(t.getUserName(),t.getUserId());
            dictList.add(de);
        }
        return AjaxResult.success(dictList);
    }

    /**
     * 按部门查询用户（含下级）
     */
    @GetMapping("/deptSelect/{deptId}")
    public AjaxResult userDeptSelect(@PathVariable(value = "deptId", required = false) String deptId)
    {
        SysUser user = new SysUser();
        user.setDeptAncestors(deptId);  //查询包含此部门ID 的用户
        List<SysUser> list = userService.selectUserAll(user);
        return AjaxResult.success(list);
    }



    /**
     * 根据用户编号获取详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = { "/", "/{userId}" })
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) String userId)
    {
        AjaxResult ajax = AjaxResult.success();
        //todo: add lookup
        List<SysRole> roles = roleService.selectRoleAll();
       // ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.put("roles", roles );
        ajax.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId))
        {
            SysUser user = userService.selectUserById(userId);
            ajax.put(AjaxResult.DATA_TAG, userService.selectUserById(userId));
            ajax.put("postIds", user.getPostIds());
            ajax.put("roleIds", user.getRoleIds());
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    //@PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user)
    {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName())))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (StringUtils.isNotEmpty(user.getPhoneNumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    //@PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user)
    {
       // userService.checkUserAllowed(user);
        if (StringUtils.isNotEmpty(user.getPhoneNumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    //@PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable String[] userIds)
    {
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
   // @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 状态修改
     */
    //@PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUser(user));
    }
}
