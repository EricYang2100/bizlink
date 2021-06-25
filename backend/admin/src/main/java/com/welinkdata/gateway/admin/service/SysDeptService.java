package com.welinkdata.gateway.admin.service;

import com.welinkdata.gateway.admin.repository.SysDeptRepository;
import com.welinkdata.gateway.admin.repository.SysRoleRepository;
import com.welinkdata.gateway.common.constant.UserConstants;
import com.welinkdata.gateway.common.core.domain.TreeSelect;
import com.welinkdata.gateway.common.core.domain.entity.SysDept;
import com.welinkdata.gateway.common.core.domain.entity.SysMenu;
import com.welinkdata.gateway.common.exception.CustomException;
import com.welinkdata.gateway.common.utils.StringUtils;
import com.welinkdata.gateway.goods.domain.ProductCategory;
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
public class SysDeptService {
    private static final Logger log = LoggerFactory.getLogger(SysDeptService.class);

    @Autowired
    SysDeptRepository sysDeptRepository;


    /**
     * 查询部门管理数据     *
     * @param dept 部门信息
     * @return 部门信息集合
     */

    public List<SysDept> selectDeptList(SysDept dept)
    {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("deptName", startsWith())
                .withMatcher("status",   exact())
                ;

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC,"parentId"));
        orders.add(new Sort.Order(Sort.Direction.ASC,"orderNum"));


        Example<SysDept> example = Example.of(dept,matcher);

        List<SysDept> pc = sysDeptRepository.findAll(example,Sort.by(orders));

        return pc;
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */

    public SysDept selectDeptById(String deptId)
    {
        return sysDeptRepository.findById(deptId).orElse(null);
    }


    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */

    public String checkDeptNameUnique(SysDept dept)
    {

        SysDept info = sysDeptRepository.findByDeptName(dept.getDeptName());

        if (StringUtils.isNotNull(info) )
        {
            if( info.getDeptId().equals(dept.getDeptId()))
                return UserConstants.UNIQUE;

            return UserConstants.NOT_UNIQUE;
        }

        return UserConstants.UNIQUE;


    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */

    public SysDept insertDept(SysDept dept)
    {
        SysDept info = sysDeptRepository.findById(dept.getParentId()).orElse(null);
        if( StringUtils.isNotNull(info)) {
            // 如果父节点不为正常状态,则不允许新增子节点
            if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
                throw new CustomException("部门停用，不允许新增");
            }
            String ancestors = info.getAncestors();
            if(ancestors == null ){
                dept.setAncestors(dept.getParentId());
            }else {
                dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
            }
        }

        return sysDeptRepository.insert(dept);
    }

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */

    public SysDept updateDept(SysDept dept)
    {
        SysDept newParentDept = null;
        if(! "0".equals(dept.getParentId())) {
             newParentDept = sysDeptRepository.findById(dept.getParentId()).orElse(null);
        }
        SysDept oldDept = sysDeptRepository.findById(dept.getDeptId()).orElse(null);
        String newAncestors = "";
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept))
        {
            newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();



        }
        String oldAncestors = oldDept.getAncestors();
        dept.setAncestors(newAncestors);
        SysDept result = sysDeptRepository.save(dept);

        updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        return result;
    }

    /**
     * 递归更新子节点的父级
     * todo:
     * @param deptId
     * @param newAncestors
     * @param oldAncestors
     */
    private void updateDeptChildren(String deptId, String newAncestors, String oldAncestors) {

    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */

    public void deleteDeptById(String deptId)
    {

         sysDeptRepository.deleteById(deptId);
    }

    public boolean hasChildByDeptId(String deptId) {
        List<SysDept> list = sysDeptRepository.findAllByParentId(deptId);
        return  list.size()>0;
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */

    public List<SysDept> buildDeptTree(List<SysDept> depts)
    {
        List<SysDept> returnList = new ArrayList<SysDept>();
        List<String> tempList = new ArrayList<String>();
        for (SysDept dept : depts)
        {
            tempList.add(dept.getDeptId());
        }
        for (Iterator<SysDept> iterator = depts.iterator(); iterator.hasNext();)
        {
            SysDept dept = (SysDept) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId()))
            {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = depts;
        }
        return returnList;
    }


    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t)
    {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList)
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
    private List<SysDept> getChildList(List<SysDept> list, SysDept t)
    {
        List<SysDept> tlist = new ArrayList<SysDept>();
        Iterator<SysDept> it = list.iterator();
        while (it.hasNext())
        {
            SysDept n = (SysDept) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().equals(t.getDeptId()))
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }
    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */

    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts)
    {
        List<SysDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }


}
