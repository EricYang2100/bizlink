package com.welinkdata.gateway.system.service;


import com.welinkdata.gateway.system.repository.SysMenuRepository;
import com.welinkdata.gateway.common.constant.UserConstants;
import com.welinkdata.gateway.common.core.domain.TreeSelect;
import com.welinkdata.gateway.common.core.domain.entity.SysMenu;
import com.welinkdata.gateway.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

@Service
public class SysMenuService {

    private static final Logger log = LoggerFactory.getLogger(SysMenuService.class);

    @Autowired
    SysMenuRepository sysMenuRepository;

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */

    public SysMenu insertMenu(SysMenu menu)
    {
        return sysMenuRepository.insert(menu);
    }

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */

    public SysMenu updateMenu(SysMenu menu)
    {
        return sysMenuRepository.save(menu);
    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public void deleteMenuById(String menuId)
    {
         sysMenuRepository.deleteById(menuId);
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */

    public String checkMenuNameUnique(SysMenu menu)
    {

        SysMenu info = sysMenuRepository.findByMenuName(menu.getMenuName());

        if (StringUtils.isNotNull(info) )
        {
            if( info.getMenuId().equals(menu.getMenuId()))
                return UserConstants.UNIQUE;

            return UserConstants.NOT_UNIQUE;
        }

        return UserConstants.UNIQUE;
    }

    /**
     * 获得产品分类列表
     * @return 结果
     */
    public List<SysMenu> selectSysMenuAll(){
        return sysMenuRepository.findAll();
    }


    /**
     * 根据条件分页查询分类
     *
     * @param menu 查询条件
     * @return 分类集合信息
     */

    public List<SysMenu> selectSysMenuAll(SysMenu menu)
    {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("menuName", startsWith())
                .withMatcher("status",   exact())
                ;

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC,"parentId"));
        orders.add(new Sort.Order(Sort.Direction.ASC,"orderNum"));


        Example<SysMenu> example = Example.of(menu,matcher);

        List<SysMenu> pc = sysMenuRepository.findAll(example,Sort.by(orders));
        return pc;
    }

    //todo
    public boolean hasChildByMenuId(String menuId) {
        return false;
    }

    public boolean checkMenuExistRole(String menuId) {
        return false;
    }

    public SysMenu selectMenuById(String menuId) {
       return  sysMenuRepository.findById(menuId).orElse(null);
    }


    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */

    public List<SysMenu> buildMenuTree(List<SysMenu> menus)
    {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        List<String> tempList = new ArrayList<String>();
        for (SysMenu dept : menus)
        {
            tempList.add(dept.getMenuId());
        }
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext();)
        {
            SysMenu menu = (SysMenu) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId()))
            {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */

    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus)
    {
        List<SysMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t)
    {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t)
    {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext())
        {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().equals(t.getMenuId()))
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    /**
     * 获取菜单列表（限定用户）
     * todo：限定用户
     * @param userId
     * @return
     */
    public List<SysMenu> selectMenuList(String userId) {
        return selectSysMenuAll();
    }


}
