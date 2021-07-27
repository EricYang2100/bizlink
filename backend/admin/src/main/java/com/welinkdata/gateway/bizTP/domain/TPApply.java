package com.welinkdata.gateway.bizTP.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.welinkdata.gateway.common.core.domain.MongoEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@ToString
@Document(collection = "bizTP_TPInvite")
/**
 * 合作伙伴连接申请，通过扫描群邀二维码（或访问url）
 */
public class TPApply extends MongoEntity {
    @Id
    private String id;


    /** 申请时间 */
    @JsonFormat(timezone= "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;


    /** 审批时间 */
    @JsonFormat(timezone= "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approveTime;

    /**
     * 审批状态（0待审批  1审批未通过  2. 审批通过  ）
     */
    private String approveStatus;


    private String tpName;

    private String contactName;

    private String contactPhone;

    private String tpInviteURL;


    /**
     * 审批成功后的的合作伙伴ID
     */
    private String tpId;
}
