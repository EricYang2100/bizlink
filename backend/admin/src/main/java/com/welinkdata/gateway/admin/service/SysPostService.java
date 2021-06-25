package com.welinkdata.gateway.admin.service;

import com.welinkdata.gateway.admin.domain.SysConfig;
import com.welinkdata.gateway.admin.repository.SysPostRepository;

import com.welinkdata.gateway.common.constant.UserConstants;

import com.welinkdata.gateway.common.core.domain.entity.SysPost;
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
public class SysPostService {


    private static final Logger log = LoggerFactory.getLogger(SysPostService.class);

    @Autowired
    SysPostRepository sysPostRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位信息集合
     */
    public Page selectPostList(SysPost post, PageRequest pageable) {
        Query query = new Query();
        if(StringUtils.isNotEmpty(post.getPostCode())) {
            query.addCriteria(Criteria.where("postCode").regex(".*"+post.getPostCode()+".*"));
        }
        if(StringUtils.isNotEmpty(post.getPostName())) {
            query.addCriteria(Criteria.where("postName").regex(".*"+post.getPostName()+".*"));
        }
        if(StringUtils.isNotEmpty(post.getStatus())) {
            query.addCriteria(Criteria.where("status").is(post.getStatus()));
        }


        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));


        query.with(Sort.by(orders));
        int count = (int) mongoTemplate.count(query, SysPost.class);


        query.with(pageable);
        List<SysPost> result = mongoTemplate.find(query,SysPost.class);

        Page page = new PageImpl(result, pageable,  count);

        return page;
    }
    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */

    public List<SysPost> selectPostAll() {
        return sysPostRepository.findAll();
    }


    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */

    public SysPost selectPostById(String postId) {
        return sysPostRepository.findById(postId).orElse(null);
    }


    /**
     * 校验岗位名称是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */

    public String checkPostNameUnique(SysPost post) {
        SysPost info = sysPostRepository.findByPostName(post.getPostName());

        if (StringUtils.isNotNull(info)) {
            if (info.getPostId().equals(post.getPostId()))
                return UserConstants.UNIQUE;

            return UserConstants.NOT_UNIQUE;
        }

        return UserConstants.UNIQUE;
    }


    /**
     * 校验岗位编码是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */

    public String checkPostCodeUnique(SysPost post) {
        SysPost info = sysPostRepository.findByPostCode(post.getPostCode());

        if (StringUtils.isNotNull(info)) {
            if (info.getPostId().equals(post.getPostId()))
                return UserConstants.UNIQUE;

            return UserConstants.NOT_UNIQUE;

        }
        return UserConstants.UNIQUE;
    }

    /**
     * 删除岗位信息
     *
     * @param postId 岗位ID
     * @return 结果
     */

    public void deletePostById(String postId) {
        sysPostRepository.deleteById(postId);
    }


    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */

    public SysPost insertPost(SysPost post)
    {
        return sysPostRepository.insert(post);
    }

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */

    public SysPost updatePost(SysPost post)
    {
        return sysPostRepository.save(post);
    }


    public int deletePostByIds(String[] postIds) {
        int count = 0;
        for(String postId:postIds){
            sysPostRepository.deleteById(postId);
            count++;
        }
        return count;
    }
}