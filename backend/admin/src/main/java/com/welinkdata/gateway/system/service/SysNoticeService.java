package com.welinkdata.gateway.system.service;

import com.welinkdata.gateway.system.domain.SysNotice;
import com.welinkdata.gateway.system.repository.SysNoticeRepository;
import com.welinkdata.gateway.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysNoticeService {

    @Autowired
    private SysNoticeRepository noticeMapper;

    @Autowired
    private MongoTemplate mongoTemplate;
    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */

    public SysNotice selectNoticeById(String noticeId)
    {

        return noticeMapper.findById(noticeId).orElse(null);
    }

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */

//    public List<SysNotice> selectNoticeList(SysNotice notice)
//    {
//
//        return noticeMapper.findAll();
//    }


    public Page selectNoticeList(SysNotice notice, PageRequest pageable) {
        Query query = new Query();
        if(StringUtils.isNotEmpty(notice.getNoticeTitle())) {
            query.addCriteria(Criteria.where("noticeTitle").regex(".*"+notice.getNoticeTitle()+".*"));
        }
        if(StringUtils.isNotEmpty(notice.getCreateBy())) {
            query.addCriteria(Criteria.where("createBy").regex(".*"+notice.getCreateBy()+".*"));
        }
        if(StringUtils.isNotEmpty(notice.getNoticeType())) {
            query.addCriteria(Criteria.where("noticeType").is(notice.getNoticeType()));
        }



        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));


        query.with(Sort.by(orders));
        int count = (int) mongoTemplate.count(query, SysNotice.class);


        query.with(pageable);
        List<SysNotice> result = mongoTemplate.find(query,SysNotice.class);

        Page page = new PageImpl(result, pageable,  count);

        return page;
    }
    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */

    public SysNotice insertNotice(SysNotice notice)
    {

        return noticeMapper.insert(notice);
    }

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */

    public SysNotice updateNotice(SysNotice notice)
    {
        return noticeMapper.save(notice);
    }

    /**
     * 删除公告对象
     *
     * @param noticeId 公告ID
     * @return 结果
     */

    public void deleteNoticeById(String noticeId)
    {
        noticeMapper.deleteById(noticeId);
    }

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */

    public int deleteNoticeByIds(String[] noticeIds)
    {
        int count = 0;
        for( String noticeId: noticeIds) {
            noticeMapper.deleteById(noticeId);
            count++;;
        }
        return count;
    }
}
