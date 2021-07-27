package com.welinkdata.gateway.bizTP.service;

import com.welinkdata.gateway.bizTP.domain.TPInvite;
import com.welinkdata.gateway.bizTP.repository.TPInfoRepository;
import com.welinkdata.gateway.bizTP.repository.TPInviteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TPInviteService {
    private static final Logger log = LoggerFactory.getLogger(TPInviteService.class);

    @Autowired
    TPInviteRepository inviteRepository;

    @Autowired
    TPInfoRepository infoRepository;

    /**
     * 查询邀请
     * @param invite
     * @return
     */
    public List<TPInvite> findInviteList(TPInvite invite){
        return null;
    }


    /**
     * 向某个TP 发起邀请
     * 1. 检查是否存在历史邀请，
     * 2. 更新TPInfo 最后邀请时间
     * @param tpId
     * @return
     */
    public TPInvite getTPInvite(String tpId){
        return null;
    }

    /**
     * 向某个TP 发起邀请
     * 1. 检查是否存在历史邀请，
     * 2. 更新TPInfo 最后邀请时间
     * @param invite
     * @return
     */
    public TPInvite inviteTP(TPInvite invite){
        return null;
    }
}
