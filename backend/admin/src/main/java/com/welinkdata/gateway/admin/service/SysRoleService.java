package com.welinkdata.gateway.admin.service;


import com.welinkdata.gateway.admin.domain.SysConfig;
import com.welinkdata.gateway.admin.repository.SysRoleRepository;
import com.welinkdata.gateway.common.constant.UserConstants;
import com.welinkdata.gateway.common.core.domain.entity.SysDept;
import com.welinkdata.gateway.common.core.domain.entity.SysRole;
import com.welinkdata.gateway.common.exception.CustomException;
import com.welinkdata.gateway.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SysRoleService {

    private static final Logger log = LoggerFactory.getLogger(SysRoleService.class);

    @Autowired
    SysRoleRepository sysRoleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 根据条件分页查询角色数据     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    public Page selectRoleList(SysRole role, PageRequest pageable) {
        Query query = new Query();
        if(StringUtils.isNotEmpty(role.getRoleName())) {
            query.addCriteria(Criteria.where("roleName").regex(".*"+role.getRoleName()+".*"));
        }
        if(StringUtils.isNotEmpty(role.getRoleKey())) {
            query.addCriteria(Criteria.where("roleKey").regex(".*"+role.getRoleKey()+".*"));
        }
        if(StringUtils.isNotEmpty(role.getStatus())) {
            query.addCriteria(Criteria.where("status").is(role.getStatus()));
        }


        if(StringUtils.isNotNull(role.getParams().get("beginTime"))) {
            Date beginTime=new Date(0);
            Date endTime=new Date();
            try {
                beginTime = new SimpleDateFormat("yyyy-MM-dd")
                        .parse((String) role.getParams().get("beginTime"));
                endTime = new SimpleDateFormat("yyyy-MM-dd")
                        .parse((String) role.getParams().get("endTime"));

            }catch (Exception e){
                log.error("date format error",e);
            }
            query.addCriteria(Criteria.where("createTime")
                    .gte(beginTime)
                    .lte(endTime));
        }

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC,"roleSort"));


        query.with(Sort.by(orders));
        int count = (int) mongoTemplate.count(query, SysRole.class);


        query.with(pageable);
        List<SysRole> result = mongoTemplate.find(query,SysRole.class);

        Page page = new PageImpl(result, pageable,  count);

        return page;
    }


    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    public List<SysRole> selectRoleAll(){
        return sysRoleRepository.findAll();
    }


    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    public SysRole selectRoleById(String roleId)
    {

        return sysRoleRepository.findById(roleId).orElse(null);
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    public String checkRoleNameUnique(SysRole role){
        SysRole info = sysRoleRepository.findByRoleName(role.getRoleName());

        if (StringUtils.isNotNull(info) )
        {
            if( info.getRoleId().equals(role.getRoleId()))
                return UserConstants.UNIQUE;

            return UserConstants.NOT_UNIQUE;
        }

        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */

    public String checkRoleKeyUnique(SysRole role){
        SysRole info = sysRoleRepository.findByRoleKey(role.getRoleKey());

        if (StringUtils.isNotNull(info) )
        {
            if( info.getRoleId().equals(role.getRoleId()))
                return UserConstants.UNIQUE;

            return UserConstants.NOT_UNIQUE;
        }

        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    public void checkRoleAllowed(SysRole role)
    {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin())
        {
            throw new CustomException("不允许操作超级管理员角色");
        }
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */

    public SysRole insertRole(SysRole role)
    {
        // 新增角色信息
        return sysRoleRepository.insert(role);

    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public SysRole updateRole(SysRole role)
    {
        // 修改角色信息
        return sysRoleRepository.save(role);

    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */

    public int deleteRoleByIds(String[] roleIds){
        int count = 0;
        for(String roleId:roleIds){
            sysRoleRepository.deleteById(roleId);
            count++;
        }
        return count;
    }

    public String[] selectMenuListByRoleId(String roleId) {
        SysRole role = selectRoleById(roleId);
        if( StringUtils.isNotNull(role)){
            return role.getMenuIds();
        }
        return new String[0];
    }


    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    public String[] selectDeptListByRoleId(String roleId) {
        SysRole role = selectRoleById(roleId);
        if( StringUtils.isNotNull(role)){
            return role.getDeptIds();
        }
        return new String[0];
    }

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */

    public SysRole authDataScope(SysRole role)
    {
        return sysRoleRepository.save(role);
    }

}
