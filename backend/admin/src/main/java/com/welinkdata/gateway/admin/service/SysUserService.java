package com.welinkdata.gateway.admin.service;

import com.welinkdata.gateway.admin.domain.SysConfig;
import com.welinkdata.gateway.admin.repository.SysUserRepository;
import com.welinkdata.gateway.common.constant.UserConstants;
import com.welinkdata.gateway.common.core.domain.entity.SysUser;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SysUserService {

    private static final Logger log = LoggerFactory.getLogger(SysUserService.class);

    @Autowired
    SysUserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    /**
     * 根据条件分页查询用户列表
     * todo:部门查询条件
     * @param user 用户信息
     * @return 用户信息集合信息
     */

    public Page selectUserList(SysUser user, PageRequest pageable) {
        Query query = new Query();
        if(StringUtils.isNotEmpty(user.getUserName())) {
            query.addCriteria(Criteria.where("userName").regex(".*"+user.getUserName()+".*"));
        }
        if(StringUtils.isNotEmpty(user.getPhoneNumber())) {
            query.addCriteria(Criteria.where("phoneNumber").regex(".*"+user.getPhoneNumber()+".*"));
        }
        if(StringUtils.isNotEmpty(user.getStatus())) {
            query.addCriteria(Criteria.where("status").is(user.getStatus()));
        }




        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));


        query.with(Sort.by(orders));
        int count = (int) mongoTemplate.count(query, SysUser.class);


        query.with(pageable);
        List<SysUser> result = mongoTemplate.find(query,SysUser.class);

        Page page = new PageImpl(result, pageable,  count);

        return page;
    }
    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */

    public SysUser selectUserByUserName(String userName)
    {

        return userRepository.findByUserName(userName);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public SysUser selectUserById(String userId)
    {

        return userRepository.findById(userId).orElse(null);
    }


    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    public String checkUserNameUnique(String userName){
        SysUser info = userRepository.findByUserName(userName);
        if(StringUtils.isNotNull(info)){
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return
     */

    public String checkPhoneUnique(SysUser user){
        SysUser info = userRepository.findByPhoneNumber(user.getPhoneNumber());

        if (StringUtils.isNotNull(info) )
        {
            if( info.getUserId().equals(user.getUserId()))
                return UserConstants.UNIQUE;

            return UserConstants.NOT_UNIQUE;
        }

        return UserConstants.UNIQUE;
    }


    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    public String checkEmailUnique(SysUser user){
        SysUser info = userRepository.findByEmail(user.getEmail());

        if (StringUtils.isNotNull(info) )
        {
            if( info.getUserId().equals(user.getUserId()))
                return UserConstants.UNIQUE;

            return UserConstants.NOT_UNIQUE;
        }

        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */

    public void checkUserAllowed(SysUser user){
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin())
        {
            throw new CustomException("不允许操作超级管理员用户");
        }
    }


    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */

    public SysUser insertUser(SysUser user){
        return userRepository.insert(user);
    }


    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */

    public SysUser updateUser(SysUser user){
        return userRepository.save(user);
    }


    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */

    public int deleteUserByIds(String[] userIds){
        int count = 0;
        for(String userId:userIds){
            userRepository.deleteById(userId);
            count++;
        }
        return count;
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    public int resetUserPwd(String userName, String password) {
        SysUser user = userRepository.findByUserName(userName);
        if(StringUtils.isNotNull(user)){
            user.setPassword(password);
            userRepository.save(user);
            return 1;
        }
        return 0;
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar 头像地址
     * @return 结果
     */

    public boolean updateUserAvatar(String userName, String avatar) {
        SysUser user = userRepository.findByUserName(userName);
        if(StringUtils.isNotNull(user)){
            user.setAvatar(avatar);

            userRepository.save(user);
            return true;
        }
        return false;
    }
}
